package org.example.productservice.service;


import org.example.productservice.entity.Product;

import java.util.Optional;

public interface ProductService {
    Product createProduct(Product product);
    Optional<Product> getProductById(String id);
    Product updateProduct(String id, Product product);
    void deleteProduct(String id);
}
