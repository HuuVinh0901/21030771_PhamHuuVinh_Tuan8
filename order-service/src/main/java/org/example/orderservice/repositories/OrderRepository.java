package org.example.orderservice.repositories;
import org.example.orderservice.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
}
