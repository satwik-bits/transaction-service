package com.transaction.management.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "roles",schema = "fraud_detection_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Roles {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
}
