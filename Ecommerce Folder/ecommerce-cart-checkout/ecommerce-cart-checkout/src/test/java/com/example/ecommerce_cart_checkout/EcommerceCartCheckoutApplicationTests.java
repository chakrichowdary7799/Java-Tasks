package com.example.ecommerce_cart_checkout;

import com.example.ecommerce_cart_checkout.dto.request.CheckoutRequest;
import com.example.ecommerce_cart_checkout.dto.response.OrderSummaryResponse;
import com.example.ecommerce_cart_checkout.exception.InsufficientStockException;
import com.example.ecommerce_cart_checkout.entities.Cart;
import com.example.ecommerce_cart_checkout.entities.CartItem;
import com.example.ecommerce_cart_checkout.entities.Product;
import com.example.ecommerce_cart_checkout.repository.CartRepository;
import com.example.ecommerce_cart_checkout.repository.CouponRepository;
import com.example.ecommerce_cart_checkout.repository.OrderRepository;
import com.example.ecommerce_cart_checkout.repository.ProductRepository;
import com.example.ecommerce_cart_checkout.service.impl.CheckoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CheckoutServiceTest {

    @Mock private CartRepository cartRepository;
    @Mock private ProductRepository productRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private CouponRepository couponRepository;

    @InjectMocks private CheckoutServiceImpl checkoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckoutInsufficientStockThrowsException() {
        Long userId = 1L;
	CheckoutRequest request = new CheckoutRequest();

        Product product = new Product();
        product.setName("Test Mouse");
        product.setPrice(BigDecimal.TEN);
        product.setStockQuantity(2); // Low stock

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(5); // Requesting more than stock

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.getItems().add(item);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        assertThrows(InsufficientStockException.class, () -> checkoutService.checkout(userId, request));
        verify(orderRepository, never()).save(any());
    }
    
    @Test
    void testSuccessfulCheckoutProcessesCorrectly() {
        Long userId = 1L;
        CheckoutRequest request = new CheckoutRequest();

        Product product = new Product();
        product.setName("Test Keyboard");
        product.setPrice(new BigDecimal("50.00"));
        product.setStockQuantity(10);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.getItems().add(item);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        OrderSummaryResponse target = checkoutService.checkout(userId, request);
	
	assertNotNull(target);
        assertEquals(new BigDecimal("100.00"), target.getFinalAmount());
        verify(productRepository, times(1)).save(product);
    }
}