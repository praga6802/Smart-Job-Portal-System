package com.example.smartjobportalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterDTO {

    private String firstname;
    private String lastname;
    private String email;
    private String contact;
    private String password;
}
