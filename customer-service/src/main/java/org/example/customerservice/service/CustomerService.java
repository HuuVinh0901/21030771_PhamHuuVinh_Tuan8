package org.example.customerservice.service;

import org.example.customerservice.entity.Customer;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<Customer> getCustomer(String id);
    Mono<Customer> createCustomer(Customer customer);
}
