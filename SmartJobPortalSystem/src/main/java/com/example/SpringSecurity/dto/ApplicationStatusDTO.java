package com.example.SpringSecurity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationStatusDTO {

    private String jobTitle;
    private String companyName;
    private Double salary;
    private String location;
    private String status;
    private LocalDateTime appliedAt;

    public ApplicationStatusDTO(String jobTitle,String companyName, Double salary, String location, String status, LocalDateTime appliedAt) {
        this.jobTitle = jobTitle;
        this.companyName=companyName;
        this.salary=salary;
        this.location=location;
        this.status = status;
        this.appliedAt = appliedAt;
    }
}
