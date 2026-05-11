package com.bank.system.controller;

import com.bank.system.entity.Account;
import com.bank.system.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts") //http://localhost:8080/api/accounts
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping //http://localhost:8080/api/accounts
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountRepository.save(account));
    }

    @GetMapping("/{id}") //http://localhost:8080/api/accounts/{id}
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
