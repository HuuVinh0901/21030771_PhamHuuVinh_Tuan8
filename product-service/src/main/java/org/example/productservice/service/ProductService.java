package org.example.productservice.service;


import org.example.productservice.entity.Product;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<Product> getProduct(String id);

    Mono<Product> createProduct(Product product);

    Mono<Product> updateStock(String productId, int quantity);
}
