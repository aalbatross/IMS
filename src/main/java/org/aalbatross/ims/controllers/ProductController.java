package org.aalbatross.ims.controllers;

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
public class ProductController {

  @Autowired private ProductService productService;

  @PutMapping("/product")
  public ResponseEntity<Product> create(@RequestBody Product product) {
    return ResponseEntity.ok(productService.create(product));
  }

  @PostMapping("/product")
  public ResponseEntity<Product> update(@RequestBody Product product) {
    return ResponseEntity.ok(productService.update(product));
  }

  @DeleteMapping("/product/{id}")
  public ResponseEntity<Long> delete(@PathVariable long id) {
    return ResponseEntity.ok(productService.delete(id));
  }

  @GetMapping("/product/{id}")
  public ResponseEntity<Product> readById(@PathVariable long id) {
    var productOptional = productService.search(id);
    return ResponseEntity.of(productOptional);
  }

  @GetMapping("/product/partName")
  public ResponseEntity<List<Product>> searchAllWithPartName(@RequestParam("q") String partName) {
    return ResponseEntity.ok(productService.searchAllWithPartName(partName));
  }

  @GetMapping("/product/manufacturer")
  public ResponseEntity<List<Product>> searchAllWithManufacturer(
      @RequestParam("q") String manufacturerName) {
    return ResponseEntity.ok(productService.searchAllWithManufacturer(manufacturerName));
  }

  @GetMapping("/product/cost")
  public ResponseEntity<List<Product>> searchAllProductWithCost(
      @RequestParam("from") double from, @RequestParam("to") double to) {
    return ResponseEntity.ok(productService.searchAllProductWithCost(from, to));
  }

  @GetMapping("/product/categories")
  public ResponseEntity<List<Product>> searchAllProductWithCategories(
      @RequestParam("name") List<String> productCategories) {
    var list =
        productCategories.stream().map(ProductCategory::valueOf).collect(Collectors.toList());
    return ResponseEntity.ok(productService.searchAllProductWithCategories(list));
  }
}
