package com.example.smartjobportalsystem.dto;

import com.example.smartjobportalsystem.entity.JobApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationStatusDTO {

    private Integer candidateId;
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
    private String skills;
    private String experience;

    private Integer jobId;
    private String jobTitle;

    public JobApplicationStatusDTO(JobApplication job){
        this.candidateId=job.getCandidate().getCandidateId();
        this.firstName=job.getCandidate().getFirstname();
        this.lastName=job.getCandidate().getLastname();
        this.email=job.getCandidate().getEmail();
        this.contact=job.getCandidate().getContact();
        this.skills=job.getCandidate().getSkills();
        this.experience=job.getCandidate().getExperience();
        this.jobId=job.getJob().getJobId();
        this.jobTitle=job.getJob().getTitle();
    }
}
