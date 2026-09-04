package com.example.smartjobportalsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerifyCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pendingCandidateId;

    private String firstname;

    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    private String contact;

    private String password;

    private String dateOfBirth;

    private String experience;

    private String skills;

    private LocalDateTime createdAt;
}
