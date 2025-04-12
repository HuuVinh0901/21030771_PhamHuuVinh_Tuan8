package org.example.productservice.service.impl;



import org.example.productservice.entity.Product;
import org.example.productservice.repositories.ProductRepository;
import org.example.productservice.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repository;

    @Override
    public Product createProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public Optional<Product> getProductById(String id) {
        return repository.findById(id);
    }

    @Override
    public Product updateProduct(String id, Product product) {
        Optional<Product> existing = repository.findById(id);
        if (existing.isPresent()) {
            Product updated = existing.get();
            updated.setName(product.getName());
            updated.setPrice(product.getPrice());
            updated.setDescription(product.getDescription());
            updated.setStock(product.getStock());
            return repository.save(updated);
        }
        throw new RuntimeException("Product not found");
    }

    @Override
    public void deleteProduct(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    @RabbitListener(queues = "order-created-queue")
    public void handleOrderCreated(OrderCreatedEvent event) {
        event.getItems().forEach(item -> {
            productRepository.findById(item.getProductId()).ifPresent(product -> {
                product.setStock(product.getStock() - item.getQuantity());
                repository.save(product);
            });
        });
    }
}
