package com.example.smartjobportalsystem.dto;

import com.example.smartjobportalsystem.entity.Job;
import com.example.smartjobportalsystem.entity.JobApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusDTO {

    private String jobTitle;
    private String companyName;
    private Double salary;
    private String location;
    private String status;
    private String experience;
    private LocalDateTime appliedAt;


    public ApplicationStatusDTO(JobApplication application){
        Job job = application.getJob();
        this.jobTitle=job.getTitle();
        this.companyName=job.getCompany().getName();
        this.salary=job.getSalary();
        this.location=job.getLocation();
        this.status=job.getStatus();
        this.experience=job.getExperience();
        this.appliedAt=application.getAppliedAt();
    }

}
