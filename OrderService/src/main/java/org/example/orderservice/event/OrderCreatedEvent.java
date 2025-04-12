package org.example.orderservice.event;

import lombok.Data;
import org.example.orderservice.entity.OrderItem;

import java.util.Date;
import java.util.List;

@Data
public class OrderCreatedEvent {
    private String orderId;
    private String customerId;
    private List<OrderItem> items;
    private Double totalPrice;
    private String status;
    private Date createdAt;
}
