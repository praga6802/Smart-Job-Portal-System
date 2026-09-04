package com.example.smartjobportalsystem.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO {

    private String token;
    private String role;
    private LocalDateTime time;
    private String message;
    private String status;


    public LoginResponseDTO(LocalDateTime time, String status, String message, String token){
        this.time=time;
        this.status=status;
        this.message=message;
        this.token=token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LoginResponseDTO(String status, String message, String jwtToken, String role) {
        this.status=status;
        this.message=message;
        this.token=jwtToken;
        this.role=role;
    }

    // failure response
    public LoginResponseDTO(LocalDateTime time, String status, String message){
        this.status=status;
        this.time=time;
        this.message=message;
    }

}