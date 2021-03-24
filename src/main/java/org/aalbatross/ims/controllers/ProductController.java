package org.aalbatross.ims.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import org.aalbatross.ims.model.Product;
import org.aalbatross.ims.model.ProductCategory;
import org.aalbatross.ims.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ims/api/v1")
@Api(
    value = "IMS Crud Service",
    description =
        "Operations pertaining to creating updating deleting and searching products in Inventory Store")
public class ProductController {

  @Autowired private ProductService productService;

  @ApiOperation(value = "Create Product", response = Iterable.class)
  @PutMapping("/product")
  public ResponseEntity<Product> create(@RequestBody Product product) {
    return ResponseEntity.ok(productService.create(product));
  }

  @ApiOperation(value = "Update existing Product", response = Iterable.class)
  @PostMapping("/product")
  public ResponseEntity<Product> update(@RequestBody Product product) {
    return ResponseEntity.ok(productService.update(product));
  }

  @ApiOperation(value = "Delete an existing product.")
  @DeleteMapping("/product/{id}")
  public ResponseEntity<Long> delete(@PathVariable long id) {
    return ResponseEntity.ok(productService.delete(id));
  }

  @ApiOperation(value = "Read an existing product by product partNumber.")
  @GetMapping("/product/{id}")
  public ResponseEntity<Product> readById(@PathVariable long id) {
    var productOptional = productService.search(id);
    return ResponseEntity.of(productOptional);
  }

  @ApiOperation(value = "Read all existing product by product partName.")
  @GetMapping("/product/partName")
  public ResponseEntity<List<Product>> searchAllWithPartName(@RequestParam("q") String partName) {
    return ResponseEntity.ok(productService.searchAllWithPartName(partName));
  }

  @ApiOperation(value = "Read all existing product by product by manufacturer name.")
  @GetMapping("/product/manufacturer")
  public ResponseEntity<List<Product>> searchAllWithManufacturer(
      @RequestParam("q") String manufacturerName) {
    return ResponseEntity.ok(productService.searchAllWithManufacturer(manufacturerName));
  }

  @ApiOperation(value = "Read all existing product by product between cost.")
  @GetMapping("/product/cost")
  public ResponseEntity<List<Product>> searchAllProductWithCost(
      @RequestParam("from") double from, @RequestParam("to") double to) {
    return ResponseEntity.ok(productService.searchAllProductWithCost(from, to));
  }

  @ApiOperation(value = "Read all existing product by product by categories.")
  @GetMapping("/product/categories")
  public ResponseEntity<List<Product>> searchAllProductWithCategories(
      @RequestParam("name") List<String> productCategories) {
    var list =
        productCategories.stream().map(ProductCategory::valueOf).collect(Collectors.toList());
    return ResponseEntity.ok(productService.searchAllProductWithCategories(list));
  }
}
