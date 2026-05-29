package com.example.smartjobportalsystem.dto;


import com.example.smartjobportalsystem.entity.RefreshToken;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class LoginResponse {

    private String token;
    private String role;
    private RefreshToken refreshToken;

    public LoginResponse(String jwtToken, String role, RefreshToken refreshToken) {
        this.token=jwtToken;
        this.role=role;
        this.refreshToken=refreshToken;
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
}