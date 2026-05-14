package com.example.ecommerce_cart_checkout.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    private String code;
    
    @Column(name = "discount_type")
    private String discountType; // FLAT or PERCENTAGE
    
    @Column(name = "discount_value")
    private BigDecimal discountValue;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}