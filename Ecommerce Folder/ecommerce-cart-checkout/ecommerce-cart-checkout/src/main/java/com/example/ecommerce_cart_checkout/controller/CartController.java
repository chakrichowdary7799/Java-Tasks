package com.example.ecommerce_cart_checkout.controller;

import com.example.ecommerce_cart_checkout.dto.request.CartItemRequest;
import com.example.ecommerce_cart_checkout.dto.response.CartResponse;
import com.example.ecommerce_cart_checkout.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart") //http://localhost:8080/api/cart
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) { this.cartService = cartService; }

    @PostMapping("/{userId}") //http://localhost:8080/api/cart/1
    public ResponseEntity<CartResponse> addToCart(@PathVariable Long userId, @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @PutMapping("/{userId}/items/{productId}") //http://localhost:8080/api/cart/1/items/2?quantity=5
    public ResponseEntity<CartResponse> updateQuantity(@PathVariable Long userId, @PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateQuantity(userId, productId, quantity));
    }

    @DeleteMapping("/{userId}/items/{productId}") //http://localhost:8080/api/cart/1/items/2
    public ResponseEntity<CartResponse> removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }

    @GetMapping("/{userId}") //http://localhost:8080/api/cart/1
    public ResponseEntity<CartResponse> viewCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.viewCart(userId));
    }
}