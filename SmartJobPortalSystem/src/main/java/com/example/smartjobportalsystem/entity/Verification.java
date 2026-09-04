package com.example.smartjobportalsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer verificationId;

    @Column(name="email",nullable = false)
    private String email;


    @Column(name="otp", nullable = false)
    private String otp;

    @Column(name = "is_used",nullable = false)
    private boolean isUsed;

    @Column(name="is_verified",nullable = false)
    private boolean isVerified;


    @Column(name = "expires_at",nullable = false)
    private LocalDateTime expiresAt;


}
