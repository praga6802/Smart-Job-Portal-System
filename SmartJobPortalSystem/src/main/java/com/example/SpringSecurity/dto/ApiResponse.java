package com.example.SpringSecurity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private String message;
    private String details;
    private LocalDateTime localDateTime;
    private UserDTO userDTO;
    private JobDetailResponse jobDetailResponse;

    public ApiResponse(LocalDateTime localDateTime, String message, String details) {
        this.message = message;
        this.details = details;
        this.localDateTime = localDateTime;
    }


    public ApiResponse(LocalDateTime localDateTime, String message, String details, UserDTO userDTO){
        this.localDateTime=localDateTime;
        this.message=message;
        this.details=details;
        this.userDTO=userDTO;
    }

    public ApiResponse(LocalDateTime localDateTime, String message, String details, JobDetailResponse jobDetailResponse){
        this.localDateTime=localDateTime;
        this.message=message;
        this.details=details;
        this.jobDetailResponse=jobDetailResponse;
    }

}
