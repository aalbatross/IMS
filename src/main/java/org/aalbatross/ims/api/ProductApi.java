package org.aalbatross.ims.api;

import java.util.List;
import java.util.Optional;
import org.aalbatross.ims.model.Product;
import org.aalbatross.ims.model.ProductCategory;

public interface ProductApi {
  Product create(Product product);

  Product update(Product product);

  long delete(long productId);

  Optional<Product> search(long productId);

  List<Product> searchAllWithPartName(String partName);

  List<Product> searchAllWithManufacturer(String manufacturerName);

  List<Product> searchAllProductWithCost(double from, double to);

  List<Product> searchAllProductWithCategories(List<ProductCategory> productCategories);
}
