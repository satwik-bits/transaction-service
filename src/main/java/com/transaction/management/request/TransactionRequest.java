package com.transaction.management.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionRequest {

    private String transactionType;

    private Double amount;

    private String senderAccount;

    private String receiverAccount;

    private LocalDateTime requestedDate = LocalDateTime.now();
}
