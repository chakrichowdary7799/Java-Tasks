package com.banking.system.service;

import com.banking.system.dto.AccountRequestDTO;
import com.banking.system.entity.Account;

public interface AccountService {
    Account createAccount(AccountRequestDTO accountDTO);
    Account getAccountByNumber(String accountNumber);
    Account getAccountByUsername(String username);
}