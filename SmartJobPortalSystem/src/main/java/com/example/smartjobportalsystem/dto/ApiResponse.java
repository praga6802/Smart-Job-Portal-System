package com.example.smartjobportalsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private String message;
    private String details;
    private LocalDateTime timestamp;
    private UserDTO userDTO;
    private JobDetailResponse jobDetailResponse;

    public ApiResponse(LocalDateTime timestamp, String message, String details) {
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }


    public ApiResponse(LocalDateTime timestamp, String message, String details, UserDTO userDTO) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.userDTO = userDTO;
    }

    public ApiResponse(LocalDateTime timestamp, String message, String details, JobDetailResponse jobDetailResponse) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.jobDetailResponse = jobDetailResponse;
    }

}
