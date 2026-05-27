package com.banking.system.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.system.dto.AccountRequestDTO;
import com.banking.system.entity.Account;
import com.banking.system.entity.AuditLog;
import com.banking.system.exception.AccountConflictException;
import com.banking.system.exception.ResourceNotFoundException;
import com.banking.system.repository.AccountRepository;
import com.banking.system.repository.AuditLogRepository;
import com.banking.system.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, AuditLogRepository auditLogRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Account createAccount(AccountRequestDTO accountDTO) {
        if (accountRepository.findByUsername(accountDTO.getUsername()).isPresent()) {
            throw new AccountConflictException("Username already exists!");
        }

        // Generate a random unique 10-digit account number
        String accountNumber = String.format("%010d", Math.abs(UUID.randomUUID().getMostSignificantBits()) % 10000000000L);

        Account account = new Account(
                accountNumber,
                accountDTO.getOwnerName(),
                accountDTO.getUsername(),
                passwordEncoder.encode(accountDTO.getPassword()),
                accountDTO.getBalance()
        );

        Account savedAccount = accountRepository.save(account);

        auditLogRepository.save(new AuditLog(
                "ACCOUNT_CREATION",
                "Created account number " + accountNumber + " for user " + accountDTO.getUsername(),
                LocalDateTime.now()
        ));

        return savedAccount;
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account number " + accountNumber + " not found"));
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
    }
}