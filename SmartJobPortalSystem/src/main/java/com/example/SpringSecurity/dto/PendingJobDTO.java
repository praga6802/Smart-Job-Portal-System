package com.example.SpringSecurity.dto;

import lombok.Data;

import java.util.List;

@Data
public class PendingJobDTO {

    private Integer jobId;
    private String companyName;
    private String jobTitle;
    private List<String> skills;
    private String location;
    private String jobType;
    private Double salary;
    private String experience;

    public PendingJobDTO(Integer jobId, String companyName, String jobTitle, List<String> skills, String location, String jobType, Double salary, String experience) {
        this.jobId = jobId;
        this.companyName=companyName;
        this.jobTitle = jobTitle;
        this.skills = skills;
        this.location = location;
        this.jobType = jobType;
        this.salary = salary;
        this.experience = experience;
    }
}
