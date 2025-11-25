package com.example.SpringSecurity.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer applicationId;

    @ManyToOne
    @JoinColumn(name="applicantId")
    private Users applicant;

    @ManyToOne
    @JoinColumn(name="jobId")
    private Job job;
    private LocalDateTime appliedAt;
    private String status;

}
