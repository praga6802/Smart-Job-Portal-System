package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.entity.Candidate;
import com.example.smartjobportalsystem.entity.Company;
import com.example.smartjobportalsystem.entity.JobApplication;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {


    List<JobApplication> findByCandidate(Candidate candidate);


    List<JobApplication> findByJob_Company(Company company);
}
