package org.example.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.Order;
import org.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;


    @PostMapping
    public Mono<Order> createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/{id}")
    public Mono<Order> getOrder(@PathVariable String id) {
        return orderService.getOrder(id);
    }

    @PutMapping("/{id}/cancel")
    public Mono<Order> cancelOrder(@PathVariable String id) {
        return orderService.cancelOrder(id);
    }
}