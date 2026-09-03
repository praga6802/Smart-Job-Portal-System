package com.example.smartjobportalsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppPerCompanyCount {

    private Integer companyId;
    private String companyName;
    private Long applicationCount;

}
