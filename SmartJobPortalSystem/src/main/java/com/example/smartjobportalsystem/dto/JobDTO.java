package com.example.smartjobportalsystem.dto;


import lombok.Data;

import java.util.List;

@Data
public class JobDTO {
    private Integer jobId;
    private String title;
    private List<String> skills;
    private String description;
    private Double salary;
    private String experience;
    private String location;
    private String type;

    public JobDTO(Integer jobId, String title, List<String> skills, String description, Double salary,String experience, String location, String type) {
        this.jobId=jobId;
        this.title = title;
        this.skills=skills;
        this.description = description;
        this.experience=experience;
        this.salary = salary;
        this.location = location;
        this.type = type;
    }
}
