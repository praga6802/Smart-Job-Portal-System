package com.example.smartjobportalsystem.dto;

import lombok.Data;

@Data
public class EmailVerificationDTO {
    private String code;

    public EmailVerificationDTO(String code) {
        this.code = code;
    }
}
