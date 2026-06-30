package com.enterprise.coupon_engine.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.coupon_engine.entity.Coupon;
import com.enterprise.coupon_engine.exception.CouponException;
import com.enterprise.coupon_engine.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    /**
     * Creates and registers a new coupon rule in the engine.
     */
    @Transactional
    public Coupon createCoupon(Coupon coupon) {
        couponRepository.findByCode(coupon.getCode()).ifPresent(c -> {
            throw new CouponException("A coupon with code '" + coupon.getCode() + "' already exists.");
        });

        if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CouponException("Cannot create a coupon that has an expiry date in the past.");
        }

        return couponRepository.save(coupon);
    }

    /**
     * Fetches a specific coupon by its mapped code string.
     */
    @Transactional(readOnly = true)
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new CouponException("Coupon code mapping not found: " + code));
    }

    /**
     * Lists all available coupon setups recorded in the database engine.
     */
    @Transactional(readOnly = true)
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    /**
     * Deletes a coupon mapping by its database transaction identifier.
     */
    @Transactional
    public void deleteCoupon(@NonNull Long id) {
        if (!couponRepository.existsById(id)) {
            throw new CouponException("Cannot delete coupon. Tracking ID not found: " + id);
        }
        couponRepository.deleteById(id);
    }
}
