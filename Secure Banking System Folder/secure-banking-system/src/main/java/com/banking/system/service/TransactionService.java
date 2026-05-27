package com.banking.system.service;

import com.banking.system.dto.TransactionRequestDTO;
import com.banking.system.entity.Transaction;

public interface TransactionService {
    Transaction transferFunds(TransactionRequestDTO request);
}