package com.example.inventory.service;

import com.example.inventory.entity.Product;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public Product addProduct(Product product) {
        if (repository.findByName(product.getName()).isPresent()) {
            throw new RuntimeException("Duplicate product names not allowed");
        }
        return repository.save(product);
    }

    public List<Product> getAllProducts() { return repository.findAll(); }

    public Product getProductById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    public Product updateProduct(Long id, Product details) {
        Product p = getProductById(id);
        p.setName(details.getName());
        p.setCategory(details.getCategory());
        p.setPrice(details.getPrice());
        return repository.save(p);
    }

    public Product adjustStock(Long id, int amount) {
        Product p = getProductById(id);
        int newTotal = p.getQuantity() + amount;
        if (newTotal < 0) throw new RuntimeException("Stock quantity cannot go below zero");
        p.setQuantity(newTotal);
        return repository.save(p);
    }

    public List<Product> getLowStock() { return repository.findByQuantityLessThan(5); }
}
