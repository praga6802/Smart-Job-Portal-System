package com.example.smartjobportalsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRegisterDTO {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String contact;
    private String dob;
    private String skills;
    private String experience;
}
