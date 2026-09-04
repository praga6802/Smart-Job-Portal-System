package com.example.smartjobportalsystem.dto;


import com.example.smartjobportalsystem.entity.Job;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class JobRegisterDTO {

    private String title;
    private String description;
    private String skills;
    private Double salary;
    private String experience;
    private String location;
    private String type;


    public JobRegisterDTO(Job job){
        this.title=job.getTitle();
        this.description=job.getDescription();
        this.salary=job.getSalary();
        this.skills=job.getSkills();
        this.experience=job.getExperience();
        this.location=job.getLocation();
        this.type=job.getType();
    }

}
