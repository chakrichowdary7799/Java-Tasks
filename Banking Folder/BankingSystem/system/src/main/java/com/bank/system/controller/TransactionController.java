package com.bank.system.controller;

import com.bank.system.dto.TransferRequest;
import com.bank.system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestBody TransferRequest request) {
        transactionService.transfer(request.getFromAccount(), request.getToAccount(), request.getAmount());
        return ResponseEntity.ok("Transfer successful");
    }
}
