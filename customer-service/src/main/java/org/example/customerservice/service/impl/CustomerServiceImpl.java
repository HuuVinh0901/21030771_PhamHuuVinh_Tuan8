package org.example.customerservice.service.impl;

import org.example.customerservice.entity.Customer;
import org.example.customerservice.repositories.CustomerRepository;
import org.example.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Mono<Customer> getCustomer(String id) {
        return customerRepository.findById(id);
    }

    @Override
    public Mono<Customer> createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

}
