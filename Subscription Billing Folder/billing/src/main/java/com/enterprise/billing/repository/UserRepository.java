package com.enterprise.billing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.billing.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByEmail(String email);
}