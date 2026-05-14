package com.example.ecommerce_cart_checkout.service.impl;

import com.example.ecommerce_cart_checkout.dto.request.CartItemRequest;
import com.example.ecommerce_cart_checkout.dto.response.CartResponse;
import com.example.ecommerce_cart_checkout.exception.ResourceNotFoundException;
import com.example.ecommerce_cart_checkout.entities.Cart;
import com.example.ecommerce_cart_checkout.entities.CartItem;
import com.example.ecommerce_cart_checkout.entities.Product;
import com.example.ecommerce_cart_checkout.repository.CartItemRepository;
import com.example.ecommerce_cart_checkout.repository.CartRepository;
import com.example.ecommerce_cart_checkout.repository.ProductRepository;
import com.example.ecommerce_cart_checkout.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });
    }

    @Override
    @Transactional
    public CartResponse addToCart(Long userId, CartItemRequest request) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + request.getQuantity());
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            cart.getItems().add(item);
        }
        cartRepository.save(cart);
        return viewCart(userId);
    }

    @Override
    @Transactional
    public CartResponse updateQuantity(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not in cart"));

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return viewCart(userId);
    }

    @Override
    @Transactional
    public CartResponse removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
        return viewCart(userId);
    }

    @Override
    public CartResponse viewCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
        List<CartResponse.ItemDetails> details = cart.getItems().stream().map(item -> {
            BigDecimal price = item.getProduct().getPrice();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
            return new CartResponse.ItemDetails(
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getQuantity(),
                    price,
                    subtotal
            );
        }).collect(Collectors.toList());

        BigDecimal grandTotal = details.stream()
                .map(CartResponse.ItemDetails::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(details, grandTotal);
    }
}