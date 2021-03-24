package org.aalbatross.ims.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.aalbatross.ims.model.Product;
import org.aalbatross.ims.model.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

  private String path = "/ims/api/v1/product";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  public void createProductSuccess() {
    var p =
        Product.builder()
            .partName("p1")
            .cost(23.0d)
            .manufacturer("m1")
            .productCategory(ProductCategory.CATEGORY1)
            .build();
    var url = "http://localhost:" + port + path;
    var requestEntity = new HttpEntity<Product>(p);
    var response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Product.class);
    assertThat(response.getBody().getPartNumber() != -1);
  }

  @Test
  public void createProductFailure() {
    var p =
        Product.builder()
            .partName("p1")
            .cost(23.0d)
            .productCategory(ProductCategory.CATEGORY1)
            .build();
    var url = "http://localhost:" + port + path;
    var requestEntity = new HttpEntity<Product>(p);
    var response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Product.class);
    assertThat(response.getStatusCode().is5xxServerError());
  }

  @Test
  public void createProductFailure2() {
    Product p = null;
    var url = "http://localhost:" + port + path;
    var requestEntity = new HttpEntity<Product>(p);
    var response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Product.class);
    assertThat(response.getStatusCode().is5xxServerError());
  }

  @Test
  public void updateProductSuccess() {
    var p =
        Product.builder()
            .partName("p1")
            .cost(23.0d)
            .manufacturer("m1")
            .productCategory(ProductCategory.CATEGORY1)
            .build();
    var url = "http://localhost:" + port + path;
    var requestEntity = new HttpEntity<Product>(p);
    var response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Product.class);
    assertThat(response.getBody().getPartNumber() != -1);
    var persistProd = response.getBody();

    persistProd.setManufacturer("mx");
    var requestEntityUpdate = new HttpEntity<Product>(persistProd);

    var responseUpdate =
        restTemplate.exchange(url, HttpMethod.POST, requestEntityUpdate, Product.class);
    assertThat(responseUpdate.getStatusCode().is2xxSuccessful());
    assertThat(responseUpdate.getBody().getManufacturer().equals("mx"));
  }

  @Test
  public void deleteProductSuccess() {
    var p =
        Product.builder()
            .partName("p1")
            .cost(23.0d)
            .manufacturer("m1")
            .productCategory(ProductCategory.CATEGORY1)
            .build();
    var url = "http://localhost:" + port + path;
    var requestEntity = new HttpEntity<Product>(p);
    var response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Product.class);
    assertThat(response.getBody().getPartNumber() != -1);
    var urlDelete = "http://localhost:" + port + path + "/" + response.getBody().getPartNumber();

    var responseDelete = restTemplate.exchange(urlDelete, HttpMethod.DELETE, null, Long.class);
    assertThat(responseDelete.getStatusCode().is2xxSuccessful());
  }

  @Test
  public void readProductSuccess() {
    var p =
        Product.builder()
            .partName("p1")
            .cost(23.0d)
            .manufacturer("m1")
            .productCategory(ProductCategory.CATEGORY1)
            .build();
    var url = "http://localhost:" + port + path;
    var requestEntity = new HttpEntity<Product>(p);
    var response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Product.class);
    assertThat(response.getBody().getPartNumber() != -1);
    var urlGet = "http://localhost:" + port + path + "/" + response.getBody().getPartNumber();

    var result = restTemplate.getForObject(urlGet, Product.class);
    assertThat(result.getPartNumber() == result.getPartNumber());
  }

  @Test
  public void searchProductSuccess() {
    var p =
        Product.builder()
            .partName("prod1")
            .cost(13.0d)
            .manufacturer("manufacture1")
            .productCategory(ProductCategory.CATEGORY2)
            .build();
    var url = "http://localhost:" + port + path;
    var requestEntity = new HttpEntity<Product>(p);
    var response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Product.class);
    assertThat(response.getBody().getPartNumber() != -1);

    // getId
    var urlGet = "http://localhost:" + port + path + "/" + response.getBody().getPartNumber();

    var result = restTemplate.getForObject(urlGet, Product.class);
    assertThat(result.getPartNumber() == result.getPartNumber());

    // getPartName
    var urlGetPartName =
        "http://localhost:" + port + path + "/partName?q=" + response.getBody().getPartName();

    var resultPartName = restTemplate.getForObject(urlGetPartName, Product[].class);
    assertThat(resultPartName[0].getPartName().equals(response.getBody().getPartName()));

    // getManufacturer
    var urlGetManufacturer =
        "http://localhost:"
            + port
            + path
            + "/manufacturer?q="
            + response.getBody().getManufacturer();

    var resultManufacturer = restTemplate.getForObject(urlGetManufacturer, Product[].class);
    assertThat(
        resultManufacturer[0].getManufacturer().equals(response.getBody().getManufacturer()));

    // getCost
    var urlGetCost =
        "http://localhost:"
            + port
            + path
            + "/cost?from="
            + response.getBody().getCost()
            + "&to="
            + response.getBody().getCost();

    var resultGetCost = restTemplate.getForObject(urlGetCost, Product[].class);
    assertThat(resultGetCost[0].getCost() == (response.getBody().getCost()));

    // getCategory
    var urlGetCategory =
        "http://localhost:"
            + port
            + path
            + "/categories?name="
            + response.getBody().getProductCategory().name();

    var resultGetCategory = restTemplate.getForObject(urlGetCategory, Product[].class);
    assertThat(resultGetCategory[0].getPartNumber() == (response.getBody().getPartNumber()));
  }
}
