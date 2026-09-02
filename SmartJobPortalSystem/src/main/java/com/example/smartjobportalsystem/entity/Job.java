package com.example.smartjobportalsystem.entity;


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

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "skills")
    private String skills;

    @Column(nullable = false,length = 2000, name = "description")
    private String description;

    @Column(nullable = false, name = "location")
    private String location;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false, name = "type")
    private String type;

    @Column(nullable = false)
    private String experience;

    @Column(nullable = false, updatable = false)
    private LocalDateTime postedDate;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name="company_id",nullable = false)
    private Company company;


    @PrePersist
    public void setDefaults(){
        if(postedDate==null)
            postedDate=LocalDateTime.now();

        if(status==null)
            status="PENDING";
    }
}
