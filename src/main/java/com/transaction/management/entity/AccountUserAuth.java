package com.transaction.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccountUserAuth {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private String ifscCode;

    private Double balance;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "account_enabled")
    private Boolean accountEnabled;
}
