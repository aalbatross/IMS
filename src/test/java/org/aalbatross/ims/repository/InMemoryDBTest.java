package org.aalbatross.ims.repository;

import java.util.List;
import org.aalbatross.ims.model.Product;
import org.aalbatross.ims.model.ProductCategory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class InMemoryDBTest {
  private static final Logger logger = LoggerFactory.getLogger(InMemoryDBTest.class);

  @Autowired private ProductRepositories repository;

  @Test
  public void givenProduct_whenSave_thenGetOk() {
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
    var persist = repository.saveAll(List.of(p1, p2, p3, p4));

    persist.stream()
        .forEach(
            product -> {
              logger.info(String.valueOf(product));
              assert (product.getPartNumber() != -1);
            });

    var productById = repository.findById(1L).orElse(Product.builder().partNumber(-1L).build());
    assert (productById.getPartNumber() != -1L);
    logger.info(String.valueOf(productById));

    var productByName = repository.findByPartName("s1");
    assert (productByName.size() == 1);
    logger.info(String.valueOf(productByName));

    var productByManufacturer = repository.findByManufacturer("m1");
    assert (productByManufacturer.size() == 3);
    logger.info(String.valueOf(productByManufacturer));

    var productByCategory =
        repository.findByProductCategoryIn(
            List.of(ProductCategory.CATEGORY1, ProductCategory.CATEGORY2));
    assert (productByCategory.size() == 4);
    logger.info(String.valueOf(productByCategory));

    var productByCost = repository.findByCostBetween(23.0d, 100.0d);
    assert (productByCost.size() == 2);
    logger.info(String.valueOf(productByCost));
  }

  @Test
  public void testCornerCases() {
    var productById = repository.findById(11L).orElse(Product.builder().partNumber(-1L).build());
    assert (productById.getPartNumber() == -1L);
    logger.info(String.valueOf(productById));

    var productByName = repository.findByPartName("product");
    assert (productByName.size() == 0);
    logger.info(String.valueOf(productByName));

    var productByNameNull = repository.findByPartName(null);
    assert (productByNameNull.size() == 0);
    logger.info(String.valueOf(productByNameNull));

    var productByManufacturer = repository.findByManufacturer("mfg");
    assert (productByManufacturer.size() == 0);
    logger.info(String.valueOf(productByManufacturer));

    var productByManufacturerNull = repository.findByManufacturer(null);
    assert (productByManufacturerNull.size() == 0);
    logger.info(String.valueOf(productByManufacturerNull));

    var productByCategory =
        repository.findByProductCategoryIn(
            List.of(ProductCategory.CATEGORY1, ProductCategory.CATEGORY2));
    assert (productByCategory.size() == 0);
    logger.info(String.valueOf(productByCategory));

    var productByCost = repository.findByCostBetween(-10.0d, -9.0d);
    assert (productByCost.size() == 0);
    logger.info(String.valueOf(productByCost));
  }
}
