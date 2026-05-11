package com.bank.system.controller;

import com.bank.system.dto.TransferRequest;
import com.bank.system.entity.Transaction;
import com.bank.system.repository.TransactionRepository;
import com.bank.system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions") //http://localhost:8080/api/transactions
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/transfer") //http://localhost:8080/api/transactions/transfer
    public ResponseEntity<String> transferFunds(@RequestBody TransferRequest request) {
        transactionService.transfer(request.getFromAccount(), request.getToAccount(), request.getAmount());
        return ResponseEntity.ok("Transfer successful");
    }

    @GetMapping("/{accountId}") //http://localhost:8080/api/transactions/{accountId}
    public List<Transaction> getHistory(@PathVariable Long accountId) {
        // Simple list retrieval for audit logs
        return transactionRepository.findAll().stream()
                .filter(t -> t.getAccountId().equals(accountId))
                .toList();
    }
}
