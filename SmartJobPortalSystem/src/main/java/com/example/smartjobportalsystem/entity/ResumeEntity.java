package com.example.smartjobportalsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@Entity
@Data
@NoArgsConstructor
public class ResumeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resumeId;

    private String fileName;
    private String filePath;

    @OneToOne
    @JoinColumn(name="userId")
    private Users user;

    public ResumeEntity(Integer resumeId, String fileName, String filePath, Users user) {
        this.resumeId = resumeId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.user = user;
    }
}
