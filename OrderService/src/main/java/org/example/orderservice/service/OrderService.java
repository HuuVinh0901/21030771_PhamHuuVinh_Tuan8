package org.example.orderservice.service;

import org.example.orderservice.entity.Order;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<Order> createOrder(Order order);
    Order getOrder(String id);
    Order cancelOrder(String id);
}
