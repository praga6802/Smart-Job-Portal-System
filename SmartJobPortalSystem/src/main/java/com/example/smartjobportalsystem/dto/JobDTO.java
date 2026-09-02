package com.example.smartjobportalsystem.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {

    private String title;
    private String description;
    private String skills;
    private Double salary;
    private String experience;
    private String location;
    private String type;

}
