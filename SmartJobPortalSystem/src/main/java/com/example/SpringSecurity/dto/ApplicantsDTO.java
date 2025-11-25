package com.example.SpringSecurity.dto;

import com.example.SpringSecurity.entity.Job;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicantsDTO {

    private Integer applicantId;
    private String applicantName;
    private String email;
    private LocalDateTime appliedAt;
    private Integer jobId;
    private String jobTitle;

    public ApplicantsDTO(String applicantName, String email, LocalDateTime appliedAt) {
        this.applicantName = applicantName;
        this.email = email;
        this.appliedAt = appliedAt;
    }

    public ApplicantsDTO(Integer appplicantId,Integer jobId,String jobTitle, String applicantName, String email, LocalDateTime appliedAt) {
        this.applicantId=appplicantId;
        this.jobId=jobId;
        this.jobTitle=jobTitle;
        this.applicantName = applicantName;
        this.email = email;
        this.appliedAt = appliedAt;
    }

}
