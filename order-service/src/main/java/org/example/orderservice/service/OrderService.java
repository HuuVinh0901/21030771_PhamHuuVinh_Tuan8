package org.example.orderservice.service;

import org.example.orderservice.entity.Order;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<Order> createOrder(Order order);
    Mono<Order> getOrder(String id);
    Mono<Order> cancelOrder(String id);
}
