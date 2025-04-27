package com.transaction.management.service;

import com.transaction.management.request.TransactionRequest;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

    void processTransaction(Long userId, TransactionRequest transactionRequest) throws Exception;
}
