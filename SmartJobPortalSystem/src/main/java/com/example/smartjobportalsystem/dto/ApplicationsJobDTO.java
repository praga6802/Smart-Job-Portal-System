package com.example.smartjobportalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationsJobDTO {

    private Integer jobId;
    private String jobTitle;
    private String companyName;
    private Long applicationCount;

}
