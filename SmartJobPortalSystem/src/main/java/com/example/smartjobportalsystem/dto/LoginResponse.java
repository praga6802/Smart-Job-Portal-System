package com.example.smartjobportalsystem.dto;


import com.example.smartjobportalsystem.entity.RefreshToken;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

    private String token;
    private String role;
    private RefreshToken refreshToken;
    private LocalDateTime time;
    private String message;
    private String status;

    public LoginResponse(String status, String message,String jwtToken, String role, RefreshToken refreshToken) {
        this.status=status;
        this.message=message;
        this.token=jwtToken;
        this.role=role;
        this.refreshToken=refreshToken;
    }

    public LoginResponse(LocalDateTime time,String status, String message, String token){
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

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
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

    public LoginResponse(String status, String message, String jwtToken, String role) {
        this.status=status;
        this.message=message;
        this.token=jwtToken;
        this.role=role;
    }

    // failure response
    public LoginResponse(LocalDateTime time,String status,String message){
        this.status=status;
        this.time=time;
        this.message=message;
    }

}