package com.example.smartjobportalsystem.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO {

    private LocalDateTime time;
    private String status;
    private String message;
    private String role;
    private String token;


    public LoginResponseDTO(LocalDateTime time, String status, String message){
        this.status=status;
        this.time=time;
        this.message=message;
    }

    public LoginResponseDTO(LocalDateTime time, String status, String message, String token){
        this.status=status;
        this.time=time;
        this.message=message;
        this.token=token;
    }

}