package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.dto.AppPerCompanyCount;
import com.example.smartjobportalsystem.dto.AppPerJobCount;
import com.example.smartjobportalsystem.entity.Candidate;
import com.example.smartjobportalsystem.entity.Company;
import com.example.smartjobportalsystem.entity.JobApplication;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {


    List<JobApplication> findByCandidate(Candidate candidate);


    List<JobApplication> findByJob_Company(Company company);


    @Query("""
            SELECT new com.example.smartjobportalsystem.dto.AppPerJobCount(
                j.jobId,j.title,c.name,COUNT(a)
            )
            FROM JobApplication a
            JOIN a.job j 
            JOIN j.company c
            GROUP BY j.jobId,j.title,c.name
            """)
    Optional<List<AppPerJobCount>> getApplicationsPerJob();


    @Query("SELECT COUNT(a) from JobApplication a")
    Long getTotalApplications();


    @Query("""
           SELECT new com.example.smartjobportalsystem.dto.AppPerCompanyCount(
                c.companyId,c.name, COUNT(a)
           )
           FROM JobApplication a JOIN a.job j JOIN j.company c 
           GROUP BY c.companyId, c.name
            """)
    Optional<List<AppPerCompanyCount>> getApplicationsPerCompany();
}
