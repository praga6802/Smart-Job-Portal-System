package com.example.smartjobportalsystem.dto;

import com.example.smartjobportalsystem.entity.Job;
import lombok.Data;

@Data
public class JobResponseDTO {

    private Integer jobId;
    private String title;
    private String description;
    private String skills;
    private String location;
    private String type;
    private Double salary;
    private String experience;
    private CompanyResponseDTO company;


    public JobResponseDTO(Job job) {
        this.jobId = job.getJobId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.skills = job.getSkills();
        this.location = job.getLocation();
        this.type = job.getType();
        this.salary = job.getSalary();
        this.experience = job.getExperience();
        this.company = new CompanyResponseDTO(job.getCompany());
    }

}
