package org.example.productservice.controller;

import org.example.productservice.entity.Product;
import org.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @PostMapping
    public Mono<Product> createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }
}
