package com.transaction.management.controller;

import com.transaction.management.request.TransactionRequest;
import com.transaction.management.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/process-transaction")
    public ResponseEntity<Object> processTransaction(
            @RequestParam Long userId,
            @RequestBody TransactionRequest transactionRequest
            ){
        try {
            transactionService.processTransaction(userId, transactionRequest);
            return ResponseEntity.ok().body("Transaction successfully processed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
