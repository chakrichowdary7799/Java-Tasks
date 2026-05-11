package com.bank.system.repository;

import com.bank.system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Custom finder method to locate a customer by their unique email.
     * Spring Data JPA generates the query automatically based on the method name.
     */
    Optional<Customer> findByEmail(String email);
}