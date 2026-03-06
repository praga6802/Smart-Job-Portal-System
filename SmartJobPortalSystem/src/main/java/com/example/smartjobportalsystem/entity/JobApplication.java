package com.example.smartjobportalsystem.entity;


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
    private Integer applicationId;

    @ManyToOne
    @JoinColumn(name="applicantId")
    private Users applicant;


    @ManyToOne
    @JoinColumn(name="jobId")
    private Job job;
    private LocalDateTime appliedAt;
    private String status;

    @ManyToOne
    @JoinColumn(name="resumeId")
    private ResumeEntity resume;

    public JobApplication(Integer applicationId, Users applicant, Job job, LocalDateTime appliedAt, String status, ResumeEntity resume) {
        this.applicationId = applicationId;
        this.applicant = applicant;
        this.job = job;
        this.appliedAt = appliedAt;
        this.status = status;
        this.resume = resume;
    }

}
