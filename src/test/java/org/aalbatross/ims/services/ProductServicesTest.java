package org.aalbatross.ims.services;

import java.util.List;
import org.aalbatross.ims.model.Product;
import org.aalbatross.ims.model.ProductCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
public class ProductServicesTest {
  private static final Logger logger = LoggerFactory.getLogger(ProductServicesTest.class);
  @Autowired private ProductService productService;

  @Test
  public void test_basicCRUDTest() {
    var p1 =
        Product.builder()
            .partName("s1")
            .productCategory(ProductCategory.CATEGORY1)
            .cost(23.0d)
            .manufacturer("m1")
            .build();

    var persist = productService.create(p1);
    logger.info(String.valueOf(persist));
    assert (productService.search(persist.getPartNumber()).isPresent());
    persist.setManufacturer("m2");
    var update = productService.update(persist);

    assert (productService.search(persist.getPartNumber()).isPresent());
    assert (productService.search(persist.getPartNumber()).get().getManufacturer().equals("m2"));

    productService.delete(persist.getPartNumber());
    assert (productService.search(persist.getPartNumber()).isPresent() == false);
  }

  @Test
  public void test_cornerCreateTest() {
    var p1 =
        Product.builder()
            .partName("s1")
            .productCategory(ProductCategory.CATEGORY1)
            .cost(23.0d)
            .build();

    Assertions.assertThrows(
        DataIntegrityViolationException.class,
        () -> {
          var persist = productService.create(p1);
        });
  }

  @Test
  public void basicEndToEndServiceTest() {
    var p1 =
        Product.builder()
            .partName("s1")
            .manufacturer("m1")
            .productCategory(ProductCategory.CATEGORY1)
            .cost(123.0)
            .build();
    var p2 =
        Product.builder()
            .partName("s2")
            .manufacturer("m1")
            .productCategory(ProductCategory.CATEGORY2)
            .cost(231.0)
            .build();
    var p3 =
        Product.builder()
            .partName("s3")
            .manufacturer("m2")
            .productCategory(ProductCategory.CATEGORY1)
            .cost(73.0)
            .build();
    var p4 =
        Product.builder()
            .partName("s4")
            .manufacturer("m1")
            .productCategory(ProductCategory.CATEGORY1)
            .cost(33.0)
            .build();
    var productList = List.of(p1, p2, p3, p4);
    productList.stream().forEach(product -> productService.create(product));

    assert (productService.all().size() == 4);

    var productById =
        productService.search(11111L).orElse(Product.builder().partNumber(-1L).build());
    assert (productById.getPartNumber() == -1L);
    logger.info(String.valueOf(productById));

    var productByName = productService.searchAllWithPartName("s1");
    assert (productByName.size() == 1);
    logger.info(String.valueOf(productByName));

    var productByManufacturer = productService.searchAllWithManufacturer("m1");
    assert (productByManufacturer.size() == 3);
    logger.info(String.valueOf(productByManufacturer));

    var productByCategory =
        productService.searchAllProductWithCategories(
            List.of(ProductCategory.CATEGORY1, ProductCategory.CATEGORY2));
    assert (productByCategory.size() == 4);
    logger.info(String.valueOf(productByCategory));

    var productByCost = productService.searchAllProductWithCost(23.0d, 100.0d);
    assert (productByCost.size() == 2);
    logger.info(String.valueOf(productByCost));
  }

  @AfterEach
  public void cleanUpEach() {
    productService.all().stream().forEach(p -> productService.delete(p.getPartNumber()));
  }
}
