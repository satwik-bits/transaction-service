package com.transaction.management.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FraudProducerRequest {

    private String transactionId;

    private String transactionType;

    private Double amount;

    private String currency;

    private LocalDateTime timestamp;

    private String accNoFrom;

    private String accNoTo;
}
