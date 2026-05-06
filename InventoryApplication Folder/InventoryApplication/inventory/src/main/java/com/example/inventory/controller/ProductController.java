package com.example.inventory.controller;

import com.example.inventory.entity.Product;
import com.example.inventory.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @PostMapping //http://localhost:8080/api/products
    public Product create(@Valid @RequestBody Product p) { return service.addProduct(p); }

    @GetMapping //http://localhost:8080/api/products
    public List<Product> list() { return service.getAllProducts(); }

    @GetMapping("/{id}") //http://localhost:8080/api/products/1
    public Product view(@PathVariable Long id) { return service.getProductById(id); }

    @PutMapping("/{id}") //http://localhost:8080/api/products/1
    public Product update(@PathVariable Long id, @RequestBody Product p) { return service.updateProduct(id, p); }

    @PatchMapping("/{id}/stock") //http://localhost:8080/api/products/1/stock?amount=5
    public Product updateStock(@PathVariable Long id, @RequestParam int amount) {
        return service.adjustStock(id, amount);
    }

    @GetMapping("/low-stock") //http://localhost:8080/api/products/low-stock
    public List<Product> lowStock() { return service.getLowStock(); }
}