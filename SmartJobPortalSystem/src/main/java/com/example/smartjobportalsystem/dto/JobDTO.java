package com.example.smartjobportalsystem.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobDTO {
    private Integer jobId;
    private String companyName;
    private String title;
    private String description;
    private List<String> skills;
    private Double salary;
    private String experience;
    private String location;
    private String type;

    //jobs by company name -> search by company jobs
    public JobDTO(Integer jobId, String title, String description, List<String> skills, Double salary, String experience, String location, String type) {
        this.jobId = jobId;
        this.title = title;
        this.description = description;
        this.skills = skills;
        this.salary = salary;
        this.experience = experience;
        this.location = location;
        this.type = type;
    }


    // search by all jobs posted by the company
    public JobDTO(String companyName, Integer jobId, String title, String description, List<String> skills, Double salary, String experience, String location, String type) {
        this.jobId = jobId;
        this.companyName=companyName;
        this.title = title;
        this.description = description;
        this.skills = skills;
        this.salary = salary;
        this.experience = experience;
        this.location = location;
        this.type = type;
    }
}
