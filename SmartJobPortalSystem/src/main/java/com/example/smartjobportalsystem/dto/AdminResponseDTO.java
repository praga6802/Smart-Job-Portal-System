package com.example.smartjobportalsystem.dto;

import com.example.smartjobportalsystem.entity.Admin;
import lombok.Data;

@Data
public class AdminResponseDTO {

    private Integer userId;
    private String firstname;
    private String lastname;
    private String email;
    private String contact;


    public AdminResponseDTO(Admin admin){
        this.userId=admin.getAdminId();
        this.firstname=admin.getFirstname();
        this.lastname=admin.getLastname();
        this.email=admin.getEmail();
        this.contact=admin.getContact();

    }

}
