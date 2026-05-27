package com.banking.system.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.system.dto.TransactionRequestDTO;
import com.banking.system.entity.Account;
import com.banking.system.entity.Transaction;
import com.banking.system.exception.InsufficientFundsException;
import com.banking.system.exception.ResourceNotFoundException;
import com.banking.system.repository.AccountRepository;
import com.banking.system.repository.TransactionRepository;
import com.banking.system.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public Transaction transferFunds(TransactionRequestDTO request) {
        // Inside your transferFunds method, verify this exact check:
        if (request.getSourceAccountNumber().equals(request.getTargetAccountNumber())) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same.");
        }


        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }

        Account sourceAccount = accountRepository.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found: " + request.getSourceAccountNumber()));

        Account destinationAccount = accountRepository.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found: " + request.getSourceAccountNumber()));

        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account: " + request.getSourceAccountNumber());
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        Transaction transaction = new Transaction();
        transaction.setSourceAccountNumber(sourceAccount.getAccountNumber());
        transaction.setTargetAccountNumber(destinationAccount.getAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        return transactionRepository.save(transaction);
    }
}