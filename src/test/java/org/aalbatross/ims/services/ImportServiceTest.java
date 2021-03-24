package org.aalbatross.ims.services;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@Slf4j
@SpringBootTest
public class ImportServiceTest {

  @Autowired private ImportService importService;

  @Test
  public void normalUploadTestSuccess() throws IOException {
    var path = ImportServiceTest.class.getClassLoader().getResource("").getPath();
    path = path + File.separator + "sample.csv";
    var bytes = FileUtils.readFileToByteArray(new File(path));
    var file =
        new MockMultipartFile(
            "sample.csv", "sample.csv", MediaType.MULTIPART_FORM_DATA_VALUE, bytes);
    var importResult = importService.importAllProductAsCSV(file);
    importResult.stream().forEach(prodIssue -> log.info(String.valueOf(prodIssue)));
    assert (importResult.size() == 8);
    var issues =
        importResult.stream()
            .filter(result -> Optional.ofNullable(result.getProduct()).isEmpty())
            .count();
    assert (issues == 0);
  }

  @Test
  public void normalUploadTestFailureBadSchema() throws IOException {
    var path = ImportServiceTest.class.getClassLoader().getResource("").getPath();
    path = path + File.separator + "sample_bad_schema.csv";
    var bytes = FileUtils.readFileToByteArray(new File(path));
    var file =
        new MockMultipartFile(
            "sample_bad_schema.csv",
            "sample_bad_schema.csv",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            bytes);
    var importResult = importService.importAllProductAsCSV(file);
    assert (importResult.size() == 8);
    importResult.stream().forEach(prodIssue -> log.info(String.valueOf(prodIssue)));
    var issues =
        importResult.stream()
            .filter(result -> Optional.ofNullable(result.getProduct()).isEmpty())
            .count();
    assert (issues == 8);
  }

  @Test
  public void normalUploadTestFailureBadSchema2() throws IOException {
    var path = ImportServiceTest.class.getClassLoader().getResource("").getPath();
    path = path + File.separator + "sample_bad_schema2.csv";
    var bytes = FileUtils.readFileToByteArray(new File(path));
    var file =
        new MockMultipartFile(
            "sample_bad_schema2.csv",
            "sample_bad_schema2.csv",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            bytes);
    var importResult = importService.importAllProductAsCSV(file);
    assert (importResult.size() == 8);
    importResult.stream().forEach(prodIssue -> log.info(String.valueOf(prodIssue)));
    var issues =
        importResult.stream()
            .filter(result -> Optional.ofNullable(result.getProduct()).isEmpty())
            .count();
    assert (issues == 8);
  }

  @Test
  public void normalUploadTestFailureHeadless() throws IOException {
    var path = ImportServiceTest.class.getClassLoader().getResource("").getPath();
    path = path + File.separator + "sample_headless.csv";
    var bytes = FileUtils.readFileToByteArray(new File(path));
    var file =
        new MockMultipartFile(
            "sample_headless.csv",
            "sample_headless.csv",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            bytes);
    var importResult = importService.importAllProductAsCSV(file);
    importResult.stream().forEach(prodIssue -> log.info(String.valueOf(prodIssue)));
    assert (importResult.size() == 7);
    var issues =
        importResult.stream()
            .filter(result -> Optional.ofNullable(result.getProduct()).isEmpty())
            .count();
    assert (issues == 7);
  }

  @Test
  public void normalUploadTestFailureEmpty() throws IOException {
    var path = ImportServiceTest.class.getClassLoader().getResource("").getPath();
    path = path + File.separator;
    var file = new MockMultipartFile("s.csv", "", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[0]);
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          importService.importAllProductAsCSV(file);
        });
  }
}
