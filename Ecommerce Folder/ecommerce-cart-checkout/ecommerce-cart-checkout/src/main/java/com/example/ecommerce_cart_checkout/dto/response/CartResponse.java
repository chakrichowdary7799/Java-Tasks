package com.example.ecommerce_cart_checkout.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {
    private List<ItemDetails> items;
    private BigDecimal totalPrice;

    public CartResponse(List<ItemDetails> items, BigDecimal totalPrice) {
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public List<ItemDetails> getItems() { return items; }
    public BigDecimal getTotalPrice() { return totalPrice; }

    public static class ItemDetails {
        private Long productId;
        private String productName;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal subTotal;

        public ItemDetails(Long productId, String productName, int quantity, BigDecimal unitPrice, BigDecimal subTotal) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.subTotal = subTotal;
        }

        public Long getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public BigDecimal getSubTotal() { return subTotal; }
    }
}