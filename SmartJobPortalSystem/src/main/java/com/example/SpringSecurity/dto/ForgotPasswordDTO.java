package com.example.SpringSecurity.dto;

import lombok.Data;

@Data
public class ForgotPasswordDTO {
    private String password;
    private String reEnterPassword;

    public ForgotPasswordDTO(String password, String reEnterPassword) {
        this.password = password;
        this.reEnterPassword = reEnterPassword;
    }
}
