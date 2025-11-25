package com.example.SpringSecurity.dto;

import lombok.Data;

@Data
public class AppPerJobCount {

    private String jobTitle;
    private Long applicationCount;

    public AppPerJobCount(String jobTitle, Long applicationCount) {
        this.jobTitle = jobTitle;
        this.applicationCount = applicationCount;
    }
}
