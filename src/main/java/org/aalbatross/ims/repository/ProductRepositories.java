package org.aalbatross.ims.repository;

import java.util.Collection;
import java.util.List;
import org.aalbatross.ims.model.Product;
import org.aalbatross.ims.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositories extends JpaRepository<Product, Long> {

  List<Product> findByPartName(String productName);

  List<Product> findByManufacturer(String manufacturer);

  List<Product> findByCostBetween(double startCost, double endCost);

  List<Product> findByProductCategoryIn(Collection<ProductCategory> categories);
}
