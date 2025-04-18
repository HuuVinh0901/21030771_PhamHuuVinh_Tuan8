package org.example.productservice.service.impl;



import org.example.productservice.entity.Product;
import org.example.productservice.repositories.ProductRepository;
import org.example.productservice.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Mono<Product> getProduct(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> updateStock(String productId, int quantity) {
        return productRepository.findById(productId)
                .flatMap(product -> {
                    if (product.getStock() < quantity) {
                        return Mono.error(new RuntimeException("Insufficient stock"));
                    }
                    product.setStock(product.getStock() - quantity);
                    return productRepository.save(product);
                });
    }
}
