package org.example.customerservice.service;

import org.example.customerservice.entity.Customer;

public interface CustomerService {
    Customer createCustomer(Customer request);
    Customer getCustomer(String id);
    Customer updateCustomer(String id, Customer request);
    void deleteCustomer(String id);
}
