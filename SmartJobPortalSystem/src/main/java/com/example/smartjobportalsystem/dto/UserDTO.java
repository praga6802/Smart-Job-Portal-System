package com.example.smartjobportalsystem.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Integer userId;
    private String username;
    private String email;
    private String role;


    public UserDTO(Integer userId, String username, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role=role;
    }

}
