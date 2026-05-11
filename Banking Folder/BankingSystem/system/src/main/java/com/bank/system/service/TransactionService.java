package com.bank.system.service;

import com.bank.system.entity.Account;
import com.bank.system.entity.Transaction;
import com.bank.system.exception.InsufficientFundsException;
import com.bank.system.repository.AccountRepository;
import com.bank.system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional // Ensures ACID compliance
    public void transfer(String fromAcc, String toAcc, BigDecimal amount) {
        Account source = accountRepository.findByAccountNumber(fromAcc)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account target = accountRepository.findByAccountNumber(toAcc)
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient balance in account: " + fromAcc);
        }

        // Update Balances
        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));

        accountRepository.save(source);
        accountRepository.save(target);

        // Audit Logging
        transactionRepository.save(Transaction.builder()
                .accountId(source.getId())
                .type("TRANSFER_OUT")
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
