package com.enterprise.coupon_engine.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.coupon_engine.entity.Coupon;
import com.enterprise.coupon_engine.service.CouponService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    /**
     * Endpoint to create a new coupon rule.
     * POST http://localhost:8080/api/v1/coupons
     */
    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@Valid @RequestBody Coupon coupon) {
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
    }

    /**
     * Endpoint to fetch a single coupon details using its unique string code.
     * GET http://localhost:8080/api/v1/coupons/code/FREESHIP10
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Coupon> getCouponByCode(@PathVariable String code) {
        Coupon coupon = couponService.getCouponByCode(code);
        return ResponseEntity.ok(coupon);
    }

    /**
     * Endpoint to list all registered coupons in the engine.
     * GET http://localhost:8080/api/v1/coupons
     */
    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    /**
     * Endpoint to delete a coupon configuration by its primary database ID.
     * DELETE http://localhost:8080/api/v1/coupons/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable @NonNull Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }
}
