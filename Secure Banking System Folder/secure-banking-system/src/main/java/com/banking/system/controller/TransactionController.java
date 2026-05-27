package com.banking.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.system.dto.TransactionRequestDTO;
import com.banking.system.entity.Transaction;
import com.banking.system.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(@Valid @RequestBody TransactionRequestDTO request) {
        Transaction transaction = transactionService.transferFunds(request);
        return ResponseEntity.ok(transaction);
    }
}