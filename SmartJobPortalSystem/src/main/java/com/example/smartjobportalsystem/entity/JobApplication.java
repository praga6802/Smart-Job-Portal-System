package com.example.smartjobportalsystem.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime appliedAt;
    private String status;

    @ManyToOne
    @JoinColumn(name="candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name="job_id")
    private Job job;


    public void setDefaults(){
        status="Applied";
        appliedAt=LocalDateTime.now();
    }

}
