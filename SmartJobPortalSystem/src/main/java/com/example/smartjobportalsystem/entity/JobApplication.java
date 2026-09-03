package com.example.smartjobportalsystem.entity;


import com.example.smartjobportalsystem.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="applied_at", updatable = false)
    private LocalDateTime appliedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @ManyToOne
    @JoinColumn(name="candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name="job_id")
    private Job job;


}
