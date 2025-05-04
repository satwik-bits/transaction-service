package com.transaction.management.request;

import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TransactionRequest {

    @Default
    private String transactionId = UUID.randomUUID().toString();



    private String transactionType;

    private Double amount;

    private String currency;

    private String accNoFrom;

    private String accNoTo;

    private LocalDateTime timestamp = LocalDateTime.now();

    private String userId;
}
