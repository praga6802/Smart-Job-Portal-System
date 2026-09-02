package com.example.smartjobportalsystem.dto;

import com.example.smartjobportalsystem.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPendingDTO {
    private Integer jobId;
    private String jobTitle;
    private String jobDescription;
    private String skills;
    private String location;
    private Double salary;
    private String type;
    private String experience;
    private LocalDateTime postedAt;
    private String companyName;

    public AdminPendingDTO(Job job){
        this.jobId = job.getJobId();
        this.jobTitle = job.getTitle();
        this.jobDescription = job.getDescription();
        this.skills = job.getSkills();
        this.location = job.getLocation();
        this.salary = job.getSalary();
        this.type = job.getType();
        this.experience = job.getExperience();
        this.postedAt = job.getPostedDate();
        if (job.getCompany() != null) {
            this.companyName = job.getCompany().getName();
        }
    }

}
