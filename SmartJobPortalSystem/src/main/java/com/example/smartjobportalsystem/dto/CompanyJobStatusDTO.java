package com.example.smartjobportalsystem.dto;

import com.example.smartjobportalsystem.entity.Job;
import com.example.smartjobportalsystem.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyJobStatusDTO {
    private Integer jobId;
    private String jobTitle;
    private String description;
    private Double salary;
    private String location;
    private String experience;
    private JobStatus status;
    private boolean isActive;
    private LocalDateTime postedDate;

    public CompanyJobStatusDTO(Job job) {
        this.jobId = job.getJobId();
        this.jobTitle = job.getTitle();
        this.description = job.getDescription();
        this.salary = job.getSalary();
        this.location = job.getLocation();
        this.experience = job.getExperience();
        this.status = job.getStatus();
        this.postedDate = job.getPostedDate();
        this.isActive=job.isActive();
    }
}
