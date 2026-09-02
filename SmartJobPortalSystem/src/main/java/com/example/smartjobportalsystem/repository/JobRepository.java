package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.entity.Company;
import com.example.smartjobportalsystem.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,Integer> {


    List<Job> findByCompany(Company company);

    List<Job> findByStatus(String status);
}
