package com.example.smartjobportalsystem.dto;


import lombok.Data;

@Data
public class MobileVerificationDTO {
    private String mobileNumber;
    private String message;

    public MobileVerificationDTO(String mobileNumber, String message) {
        this.mobileNumber = mobileNumber;
        this.message = message;
    }
}
