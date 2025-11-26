package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.dto.PendingJobDTO;
import com.example.smartjobportalsystem.entity.Job;
import com.example.smartjobportalsystem.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepo extends JpaRepository<Job,Integer> {

    Optional<Job> findByJobIdAndCompanyUserId(Integer id, Integer companyUserId);

    List<Job> findByStatus(String status);

    List<Job> findByCompany(Users company);

    List<Job> findByCompanyAndStatus(Users company, String status);




}
