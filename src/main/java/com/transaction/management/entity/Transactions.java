package com.transaction.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions",schema = "fraud_detection_db")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "type")
    private String type;

    @Column(name= "userId")
    private Long userId;

    @Column(name= "accNoFrom")
    private String accNoFrom;

    @Column(name= "accNoTo")
    private String accNoTo;

    @Column(name= "status")
    private String status;

    @Column(name= "timestamp")
    private LocalDateTime timestamp;
}
