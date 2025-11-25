package com.example.SpringSecurity.repository;

import com.example.SpringSecurity.entity.Job;
import com.example.SpringSecurity.entity.JobApplication;
import com.example.SpringSecurity.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepo extends JpaRepository<JobApplication, Integer> {
    Integer countByJob(Job job);

    @Query("SELECT j.jobTitle,COUNT(a) FROM JobApplication a JOIN a.job j GROUP BY j.jobId")
    List<Object[]> countApplicationsPerJob();

    @Query("SELECT c.username,COUNT(a) FROM JobApplication a JOIN a.job j JOIN j.company c GROUP BY c.userId")
    List<Object[]> countApplicationPerCompany();

    boolean existsByJobAndApplicant(Job job, Users company);
    boolean existsByApplicantUserIdAndJobJobId(Integer applicantId, Integer jobId);

    List<JobApplication> findByApplicant(Users applicant);

    List<JobApplication> findByJob(Job job);

    List<JobApplication> findByJobIn(List<Job> jobs);

}
