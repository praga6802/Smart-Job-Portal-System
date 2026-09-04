package com.example.smartjobportalsystem.dto;

import com.example.smartjobportalsystem.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveJobsDTO {
    private Integer jobId;
    private String jobTitle;

    public ActiveJobsDTO(Job job){
        this.jobId=job.getJobId();
        this.jobTitle=job.getTitle();
    }
}
