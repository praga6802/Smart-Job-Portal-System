package com.example.SpringSecurity.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name="jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    @Column(nullable = false)
    private String jobTitle;

    @ElementCollection
    @CollectionTable(name = "job_skills",joinColumns = @JoinColumn(name="job_id"))
    @Column(nullable = false)
    private List<String> skills;

    @Column(nullable = false,length = 2000)
    private String jobDescription;

    @Column(nullable = false)
    private String jobLocation;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false)
    private String jobType;

    @Column(nullable = false)
    private String experience;

    @Column(nullable = false)
    private LocalDateTime postedDate;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name="company_id",nullable = false)
    private Users company;

    private String companyName;


}
