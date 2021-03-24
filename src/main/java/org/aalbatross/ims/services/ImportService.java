package org.aalbatross.ims.services;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.aalbatross.ims.api.ImportApi;
import org.aalbatross.ims.configuration.ImportConfiguration;
import org.aalbatross.ims.model.ImportProductIssue;
import org.aalbatross.ims.model.Product;
import org.aalbatross.ims.model.ProductCategory;
import org.aalbatross.ims.repository.ProductRepositories;
import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ImportService implements ImportApi {
  @Autowired private ProductRepositories repository;
  @Autowired private ImportConfiguration configuration;

  @Transactional
  @Override
  public List<ImportProductIssue> importAllProductAsCSV(MultipartFile file) {
    try {
      log.info(
          "Received request to import product via CSV, importing {}.", file.getOriginalFilename());
      var csvFile = writeToFile(file);
      var prodIssues = parseCSV(csvFile);
      var issues = persist(prodIssues);
      log.info("Persisted the product list from imported CSV {}.", file.getOriginalFilename());
      return issues;
    } catch (IOException e) {
      throw new IllegalArgumentException("Provide appropriate csv file.");
    }
  }

  private File writeToFile(MultipartFile file) throws IOException {
    log.info("Copying the imported file to upload store.");
    File uploadStore = new File(configuration.getUploadStore());
    if (!uploadStore.exists()) {
      log.info("Creating upload store at path {}.", uploadStore.getAbsolutePath());
      uploadStore.mkdirs();
    }
    File writeFile =
        new File(uploadStore.getAbsolutePath() + File.separator + file.getOriginalFilename());
    Files.deleteIfExists(writeFile.toPath());
    log.info("Writing uploaded file to the store {}", writeFile.getAbsolutePath());
    writeFile.createNewFile();
    file.transferTo(writeFile);
    log.info("File {} uploading completed.", writeFile.getAbsolutePath());
    return writeFile;
  }

  private List<ImportProductIssue> parseCSV(File file) throws IOException {
    var csvFormat = CSVFormat.RFC4180.withFirstRecordAsHeader();
    if (file.exists() && file.getName().endsWith(".csv")) {
      log.info("Parsing CSV file from upload {}", file.getAbsolutePath());
      try (Reader in = new FileReader(file);
          var parser = csvFormat.parse(in)) {
        return parser.getRecords().stream()
            .map(
                record -> {
                  try {
                    var partName = record.get("partName");
                    var manufacturer = record.get("manufacturer");
                    var cost = Double.valueOf(record.get("cost"));
                    var category =
                        ProductCategory.valueOf(
                            Optional.ofNullable(record.get("productCategory"))
                                .map(val -> val.trim().toUpperCase())
                                .orElseThrow(
                                    () ->
                                        new RuntimeException(
                                            "Mapping for productCategory not found")));
                    var prod =
                        Product.builder()
                            .partName(partName)
                            .cost(cost)
                            .productCategory(category)
                            .manufacturer(manufacturer)
                            .build();
                    return ImportProductIssue.builder()
                        .lineNo(record.getRecordNumber())
                        .product(prod)
                        .build();
                  } catch (Exception ex) {
                    return ImportProductIssue.builder()
                        .lineNo(record.getRecordNumber())
                        .issue(ex.getLocalizedMessage())
                        .build();
                  }
                })
            .collect(Collectors.toList());
      }
    } else {
      throw new IllegalArgumentException("File provided not found " + file.getName());
    }
  }

  private List<ImportProductIssue> persist(List<ImportProductIssue> productIssues) {
    log.info("Persisting the Product records from the imported CSV row by row.");
    return productIssues.stream()
        .map(
            productIssue -> {
              Optional.ofNullable(productIssue.getProduct())
                  .ifPresent(
                      product -> {
                        try {
                          var p = repository.save(product);
                          log.debug("Persisting {} at row number :{}", p, productIssue.getLineNo());
                          productIssue.setProduct(p);
                        } catch (Exception ex) {
                          productIssue.setIssue(
                              productIssue.getIssue() + "," + ex.getLocalizedMessage());
                          log.debug(
                              "Found error {} at row number :{}",
                              productIssue.getIssue(),
                              productIssue.getLineNo());
                        }
                      });
              return productIssue;
            })
        .collect(Collectors.toList());
  }
}
