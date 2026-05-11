package com.bank.system.repository;

import com.bank.system.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Standard CRUD methods like .save() are inherited from JpaRepository
}