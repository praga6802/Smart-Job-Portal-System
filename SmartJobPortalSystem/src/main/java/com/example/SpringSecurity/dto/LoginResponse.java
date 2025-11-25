package com.example.SpringSecurity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponse {

    private ApiResponse apiResponse;
    private String token;


    public LoginResponse(ApiResponse apiResponse, String token) {
      this.apiResponse=apiResponse;
      this.token=token;
    }
}
