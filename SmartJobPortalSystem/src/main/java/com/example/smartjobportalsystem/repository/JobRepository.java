package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.entity.Company;
import com.example.smartjobportalsystem.entity.Job;
import com.example.smartjobportalsystem.entity.JobApplication;
import com.example.smartjobportalsystem.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,Integer> {


    List<Job> findByCompany(Company company);

    List<Job> findByStatus(JobStatus status);


    List<Job> findByStatusAndActive(JobStatus jobStatus, boolean b);

    List<Job> findByCompanyAndStatusAndActive(Company company, JobStatus jobStatus, boolean b);

    List<Job> findByCompanyAndStatus(Company company, JobStatus jobStatus);

    List<Job> findByCompanyAndActive(Company company, boolean b);
}
