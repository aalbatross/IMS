package org.aalbatross.ims.services;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.aalbatross.ims.api.ProductApi;
import org.aalbatross.ims.model.Product;
import org.aalbatross.ims.model.ProductCategory;
import org.aalbatross.ims.repository.ProductRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductService implements ProductApi {
  @Autowired private ProductRepositories repository;

  @Override
  @Transactional
  public Product create(Product product) {
    log.info("Creating new product using {}.", product);
    return repository.save(product);
  }

  @Override
  @Transactional
  public Product update(Product product) {
    log.info("Updating existing product using {}.", product);
    return repository.save(product);
  }

  @Override
  @Transactional
  public long delete(long productId) {
    log.info("Deleting product with id {}.", productId);
    repository.deleteById(productId);
    return productId;
  }

  @Override
  public Optional<Product> search(long productId) {
    log.info("Searching product with partNumber : {}.", productId);
    return repository.findById(productId);
  }

  @Override
  public List<Product> searchAllWithPartName(String partName) {
    log.info("Searching the Product repository with partName provided :{}", partName);
    return repository.findByPartName(partName);
  }

  @Override
  public List<Product> searchAllWithManufacturer(String manufacturerName) {
    log.info(
        "Searching the Product repository with manufacturerName provided :{}", manufacturerName);
    return repository.findByManufacturer(manufacturerName);
  }

  @Override
  public List<Product> searchAllProductWithCost(double from, double to) {
    log.info("Searching the Product repository with product costing between {} and {}.", from, to);
    return repository.findByCostBetween(from, to);
  }

  @Override
  public List<Product> searchAllProductWithCategories(List<ProductCategory> productCategories) {
    log.info("Searching the Product repository with product categories {}.", productCategories);
    return repository.findByProductCategoryIn(productCategories);
  }

  public List<Product> all() {
    return repository.findAll();
  }
}
