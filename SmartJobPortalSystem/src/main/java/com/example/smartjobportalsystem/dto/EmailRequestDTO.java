package com.example.smartjobportalsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailRequestDTO {

    private String toEmail;
    private String subject;
    private String description;


    public EmailRequestDTO(String toEmail, String subject, String description) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.description = description;
    }
}

