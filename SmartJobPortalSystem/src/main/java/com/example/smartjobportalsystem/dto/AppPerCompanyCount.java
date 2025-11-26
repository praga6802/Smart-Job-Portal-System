package com.example.smartjobportalsystem.dto;


import lombok.Data;

@Data
public class AppPerCompanyCount {

    private String companyName;
    private Long applicationCount;

    public AppPerCompanyCount(String companyName, Long applicationCount) {
        this.companyName = companyName;
        this.applicationCount = applicationCount;
    }
}
