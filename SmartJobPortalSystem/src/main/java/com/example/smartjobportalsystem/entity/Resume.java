package com.example.smartjobportalsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Resume{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resumeId;
    private String fileName;
    private String filePath;


    @OneToOne
    @JoinColumn(name="candidate_id")
    private Candidate candidate;

    public Resume(Integer resumeId, String fileName, String filePath, Candidate candidate) {
        this.resumeId = resumeId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.candidate = candidate;
    }
}
