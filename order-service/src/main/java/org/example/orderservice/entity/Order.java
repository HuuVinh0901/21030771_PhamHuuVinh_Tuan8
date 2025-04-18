package org.example.orderservice.entity;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Getter
@Document(collection = "orders")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String customerId;
    private String productId;
    private int quantity;
    private String status;
}