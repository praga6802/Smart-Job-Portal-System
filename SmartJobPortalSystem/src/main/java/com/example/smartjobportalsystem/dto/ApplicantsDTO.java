package com.example.smartjobportalsystem.dto;

import com.example.smartjobportalsystem.entity.Job;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicantsDTO {

    private Integer applicantId;
    private Integer applicationId;
    private String applicantName;
    private String email;
    private String status;
    private LocalDateTime appliedAt;
    private Integer jobId;
    private String jobTitle;

    public ApplicantsDTO(String applicantName, String email, LocalDateTime appliedAt) {
        this.applicantName = applicantName;
        this.email = email;
        this.appliedAt = appliedAt;
    }

    public ApplicantsDTO(Integer applicationId,Integer applicantId,Integer jobId,String jobTitle, String applicantName, String email,String status, LocalDateTime appliedAt) {
        this.applicationId=applicationId;
        this.applicantId=applicantId;
        this.jobId=jobId;
        this.jobTitle=jobTitle;
        this.applicantName = applicantName;
        this.email = email;
        this.status=status;
        this.appliedAt = appliedAt;
    }

}
