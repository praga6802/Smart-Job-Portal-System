package com.example.smartjobportalsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class JobDetailResponse {

    private Integer jobId;
    private String jobTitle;
    private String jobDescription;
    private List<String> skills;
    private String location;
    private String jobType;
    private Double salary;
    private String experience;

    public JobDetailResponse(Integer jobId, String jobTitle, String jobDescription, List<String> skills, String location, String jobType, Double salary, String experience) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.skills = skills;
        this.location = location;
        this.jobType = jobType;
        this.salary = salary;
        this.experience = experience;
    }
}
