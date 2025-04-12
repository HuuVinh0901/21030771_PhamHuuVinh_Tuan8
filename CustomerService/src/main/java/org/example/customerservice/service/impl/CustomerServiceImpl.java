package org.example.customerservice.service.impl;

import org.example.customerservice.entity.Customer;
import org.example.customerservice.repositories.CustomerRepository;
import org.example.customerservice.service.CustomerService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

  

    @Override
    public Customer createCustomer(Customer request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setPhone(request.getPhone());
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }

    @Override
    public Customer updateCustomer(String id, Customer request) {
        Customer customer = getCustomer(id);
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setPhone(request.getPhone());
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(String id) {
        Customer customer = getCustomer(id);
        customerRepository.delete(customer);
    }
}
