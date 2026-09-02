package com.example.smartjobportalsystem.dto;

import com.example.smartjobportalsystem.entity.Candidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CandidateDTO {

    private Integer candidateId;
    private String firstname;
    private String lastname;
    private String email;
    private String contact;
    private String dob;
    private String skills;
    private String experience;


    public CandidateDTO(Candidate candidate){
        this.candidateId=candidate.getCandidateId();
        this.firstname=candidate.getFirstname();
        this.lastname=candidate.getLastname();
        this.email=candidate.getEmail();
        this.contact=candidate.getContact();
        this.dob=candidate.getDateOfBirth();
        this.skills=candidate.getSkills();
        this.experience=candidate.getExperience();

    }

}
