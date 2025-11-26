package com.example.smartjobportalsystem.entity;

import com.example.smartjobportalsystem.dto.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class VerificationTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private Users user;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationType type;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean isUsed=false;


    public VerificationTable(){}

    public VerificationTable(Users user, VerificationType type, String code, LocalDateTime expiresAt) {
        this.user = user;
        this.type = type;
        this.code = code;
        this.expiresAt = expiresAt;
    }
}
