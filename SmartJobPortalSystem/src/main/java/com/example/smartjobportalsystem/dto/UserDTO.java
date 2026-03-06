package com.example.smartjobportalsystem.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Integer userId;
    private String username;
    private String email;
    private String role;
    private String mobNumber;


    public UserDTO(Integer userId, String username, String email, String role, String mobNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role=role;
        this.mobNumber=mobNumber;
    }

}
