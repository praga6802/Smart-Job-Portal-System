package com.example.smartjobportalsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRegisterDTO {
    private String name;
    private String description;
    private String email;
    private String contact;
    private String password;
    private String url;
    private String size;
    private String type;
    private String location;
    private String gst;
}
