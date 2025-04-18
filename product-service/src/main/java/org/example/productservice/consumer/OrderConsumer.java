package org.example.productservice.consumer;

import org.example.productservice.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    @Autowired
    private ProductService productService;

    @RabbitListener(queues = "order.created")
    public void handleOrderCreated(Order order) {
        productService.updateStock(order.getProductId(), order.getQuantity())
                .doOnSuccess(product -> System.out.println("Updated stock for product " + product.getId() + ": " + product.getStock()))
                .doOnError(error -> System.out.println("Error updating stock: " + error.getMessage()))
                .block(); // Chuyển reactive sang blocking vì RabbitMQ listener không reactive
    }
}
class Order {
    private String id;
    private String productId;
    private int quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}