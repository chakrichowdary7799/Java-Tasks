package com.enterprise.coupon_engine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enterprise.coupon_engine.entity.UserCoupon;

import jakarta.persistence.LockModeType;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT uc FROM UserCoupon uc WHERE uc.userId = :userId AND uc.coupon.id = :couponId")
    Optional<UserCoupon> findByUserIdAndCouponIdWithLock(@Param("userId") Long userId, @Param("couponId") Long couponId);
}
