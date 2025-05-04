package com.transaction.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account",schema = "fraud_detection_db")
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "user_id")
    private Long user_id;

    @Column(name= "account_number")
    private String accountNumber;

    @Column(name= "ifsc_code")
    private String ifsc_code;

    @Column(name= "balance")
    private Double balance;

    @Column(name= "Currency")
    private String currency;
}
