package com.example.smartjobportalsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobApplicantsResponseDTO {

    // job details
    private Integer jobId;
    private String jobTitle;


    // Candidate details
    private String firstname;
    private String lastname;
    private String email;
    private String contact;
    private String skills;
    private String experience;

    private LocalDateTime appliedAt;

    public JobApplicantsResponseDTO(LocalDateTime appliedAt, String firstname, String lastname,
                                    String email, String contact, String skills, String experience, Integer jobId, String title) {
        this.appliedAt=appliedAt;
        this.firstname=firstname;
        this.lastname=lastname;
        this.email=email;
        this.contact=contact;
        this.skills=skills;
        this.experience=experience;
        this.jobId=jobId;
        this.jobTitle=title;
    }

    public JobApplicantsResponseDTO(LocalDateTime appliedAt, String firstname, String lastname,
                                    String email, String contact, String skills, String experience) {
        this.appliedAt=appliedAt;
        this.firstname=firstname;
        this.lastname=lastname;
        this.email=email;
        this.contact=contact;
        this.skills=skills;
        this.experience=experience;
    }
}
