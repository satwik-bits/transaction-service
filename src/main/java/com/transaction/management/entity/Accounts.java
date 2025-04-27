package com.transaction.management.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Accounts {

    private String id;

    private String user_id;

    private String account_number;

    private String ifsc_code;

    private Double balance;
}
