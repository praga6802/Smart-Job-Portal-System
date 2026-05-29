package com.example.smartjobportalsystem.dto;


import com.example.smartjobportalsystem.entity.RefreshToken;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class JWTResponseDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String token;
    private String role;
    private String refreshToken;


    public JWTResponseDTO(LocalDateTime timestamp,String message,String role,String token, String refreshToken){
        this.timestamp=timestamp;
        this.message=message;
        this.token=token;
        this.role=role;
        this.refreshToken=refreshToken;
    }
}
