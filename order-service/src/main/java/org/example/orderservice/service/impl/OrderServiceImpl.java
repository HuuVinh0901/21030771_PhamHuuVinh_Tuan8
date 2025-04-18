package org.example.orderservice.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.example.orderservice.entity.Order;
import org.example.orderservice.repositories.OrderRepository;
import org.example.orderservice.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Mono<Order> createOrder(Order order) {
        Mono<Object> customerMono = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/customers/" + order.getCustomerId())
                .retrieve()
                .bodyToMono(Object.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")));

        Mono<Product> productMono = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/products/" + order.getProductId())
                .retrieve()
                .bodyToMono(Product.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")));

        return Mono.zip(customerMono, productMono)
                .flatMap(tuple -> {
                    Product product = tuple.getT2();
                    if (product.getStock() < order.getQuantity()) {
                        return Mono.error(new RuntimeException("Insufficient stock"));
                    }
                    order.setStatus("created");
                    return orderRepository.save(order)
                            .doOnSuccess(savedOrder -> {
                                // Gửi message tới RabbitMQ
                                rabbitTemplate.convertAndSend("", "order.created", savedOrder);
                            });
                });
    }

    @Override
    public Mono<Order> getOrder(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public Mono<Order> cancelOrder(String id) {
        return orderRepository.findById(id)
                .flatMap(order -> {
                    order.setStatus("cancelled");
                    return orderRepository.save(order);
                });
    }
}
@Setter
@Getter
class Product {
    private String id;
    private int stock;

}