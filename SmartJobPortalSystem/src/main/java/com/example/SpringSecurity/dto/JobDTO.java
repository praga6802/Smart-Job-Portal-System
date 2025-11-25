package com.example.SpringSecurity.dto;


import lombok.Data;

import java.util.List;

@Data
public class JobDTO {
    private String title;
    private List<String> skills;
    private String description;
    private Double salary;
    private String experience;
    private String location;
    private String type;

    public JobDTO(String title, List<String> skills, String description, Double salary,String experience, String location, String type) {
        this.title = title;
        this.skills=skills;
        this.description = description;
        this.experience=experience;
        this.salary = salary;
        this.location = location;
        this.type = type;
    }
}
