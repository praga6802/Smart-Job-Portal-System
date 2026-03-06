package com.example.smartjobportalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    private String username;
    private String email;
    private String password;
    private String role;
    private String mobNumber;


}
