package com.bank.system.service;

import com.bank.system.entity.Account;
import com.bank.system.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Finds an account by its unique account number.
     */
    public Optional<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    /**
     * Updates the balance of a specific account.
     * This is usually called within a @Transactional method.
     */
    public void updateBalance(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
        
        account.setBalance(amount);
        accountRepository.save(account);
    }

    /**
     * Retrieves the current balance for an account.
     */
    public BigDecimal getBalance(String accountNumber) {
        return getAccountByNumber(accountNumber)
                .map(Account::getBalance)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}