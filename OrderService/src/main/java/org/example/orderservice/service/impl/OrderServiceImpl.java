package org.example.orderservice.service.impl;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.event.OrderCreatedEvent;
import org.example.orderservice.repositories.OrderRepository;
import org.example.orderservice.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    private WebClient webClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public Mono<Order> createOrder(Order order) {
        // Validate customer and products
        return validateOrder(order)
                .flatMap(validatedOrder -> {
                    // Set additional fields
                    validatedOrder.setCreatedAt(new Date());
                    validatedOrder.setStatus("PENDING");
                    // Save to MongoDB
                    return Mono.just(orderRepository.save(validatedOrder));
                })
                .flatMap(savedOrder -> {
                    // Send OrderCreatedEvent to RabbitMQ
                    OrderCreatedEvent event = new OrderCreatedEvent();
                    event.setOrderId(savedOrder.getId());
                    event.setCustomerId(savedOrder.getCustomerId());
                    event.setItems(savedOrder.getItems());
                    event.setTotalPrice(savedOrder.getTotalPrice());
                    event.setStatus(savedOrder.getStatus());
                    event.setCreatedAt(savedOrder.getCreatedAt());
                    rabbitTemplate.convertAndSend("order-created-queue", event);
                    return Mono.just(savedOrder);
                });
    }

    private Mono<Order> validateOrder(Order order) {
        return checkCustomerExists(order.getCustomerId())
                .flatMap(customerExists -> {
                    if (!customerExists) {
                        return Mono.error(new RuntimeException("Invalid customer ID: " + order.getCustomerId()));
                    }
                    return Flux.fromIterable(order.getItems())
                            .flatMap(item -> getProductDetails(item.getProductId())
                                    .map(product -> {
                                        item.setPrice(product.getPrice());
                                        return item.getPrice() * item.getQuantity();
                                    }))
                            .reduce(0.0, Double::sum)
                            .map(totalPrice -> {
                                order.setTotalPrice(totalPrice);
                                return order;
                            });
                });
    }

    private Mono<Boolean> checkCustomerExists(String customerId) {
        return webClient.get()
                .uri("http://localhost:8080/customers/" + customerId)
                .retrieve()
                .bodyToMono(Object.class)
                .map(response -> true)
                .onErrorResume(e -> Mono.just(false));
    }

    private Mono<ProductResponse> getProductDetails(String productId) {
        return webClient.get()
                .uri("http://localhost:8080/products/" + productId)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("Product not found: " + productId)));
    }


    @Override
    public Order getOrder(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Override
    public Order cancelOrder(String id) {
        Order order = getOrder(id);
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot cancel order with status " + order.getStatus());
        }
        order.setStatus("CANCELLED");

//        for (OrderItem item : order.getItems()) {
//            rabbitTemplate.put("http://localhost:8081/products/" + item.getProductId() + "/stock?quantity=" + (-item.getQuantity()), null);
//        }

        return orderRepository.save(order);
    }
}
@Data

class ProductResponse {
    private String id;
    private String name;
    private Double price;
    private Integer stock;
}

