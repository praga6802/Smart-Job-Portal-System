package com.example.smartjobportalsystem.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JWTResponseDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    private String statusCode;
    private String message;
    private String token;

    public JWTResponseDTO(LocalDateTime timestamp, String statusCode, String message, String token) {
        this.timestamp = timestamp;
        this.statusCode = statusCode;
        this.message = message;
        this.token = token;
    }
}
