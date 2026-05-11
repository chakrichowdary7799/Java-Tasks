package com.bank.system.controller;

import com.bank.system.entity.Customer;
import com.bank.system.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers") //http://localhost:8080/api/customers
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping //http://localhost:8080/api/customers
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerRepository.save(customer));
    }

    @GetMapping //http://localhost:8080/api/customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}