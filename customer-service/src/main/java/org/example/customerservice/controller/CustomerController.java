package org.example.customerservice.controller;
import org.example.customerservice.entity.Customer;
import org.example.customerservice.service.CustomerService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public Mono<Customer> createCustomer(@RequestBody Customer request) {
        return customerService.createCustomer(request);
    }

    @GetMapping("/{id}")
    public Mono<Customer> getCustomer(@PathVariable String id) {
        return customerService.getCustomer(id);
    }

}

