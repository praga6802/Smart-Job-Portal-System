package com.example.smartjobportalsystem.controller;


import com.example.smartjobportalsystem.dto.JobDTO;
import com.example.smartjobportalsystem.dto.JobDetailResponse;
import com.example.smartjobportalsystem.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;


    // post job
    @PostMapping("/postJob")
    public ResponseEntity<?> postJob(@RequestBody JobDTO job, @AuthenticationPrincipal UserDetails companyUser){
        String email=companyUser.getUsername();
        return companyService.postJob(job,email);
    }

    //update job by ID
    @PutMapping("/updateJob/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Integer id,@RequestBody JobDTO job, @AuthenticationPrincipal UserDetails companyUser){
        String email=companyUser.getUsername();
        return companyService.updateJob(id,job,email);
    }

    // delete job by ID
    @DeleteMapping("/deleteJob/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Integer id, @AuthenticationPrincipal UserDetails companyUser){
        String email=companyUser.getUsername();
        return companyService.deleteJob(id,email);
    }

    //get all jobs
    @GetMapping("/getAllJobs")
    public List<JobDetailResponse> getAllJobs(@AuthenticationPrincipal UserDetails company){
        String email=company.getUsername();
        return companyService.getAllJobs(email);
    }

    //get job by id
    @GetMapping("/getJobById/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Integer id, @AuthenticationPrincipal UserDetails company){
        String email=company.getUsername();
        return companyService.getJobById(id,email);
    }

    // get applicants for the particular job
    @GetMapping("viewApplicants/{jobId}")
    public ResponseEntity<?> getApplicantsByJobId(@PathVariable Integer jobId, @AuthenticationPrincipal UserDetails company){
        String email=company.getUsername();
        return companyService.getApplicantsByJobId(jobId,email);
    }

    // get all applicants
    @GetMapping("/viewApplicants")
    public ResponseEntity<?> getAllApplicants(@AuthenticationPrincipal UserDetails company){
        String email=company.getUsername();
        return companyService.getAllApplicants(email);
    }

    // approve the applicant by applicant ID
    @PutMapping("/approveApplicant/{appli_id}")
    public ResponseEntity approveApplicant(@PathVariable Integer appli_id, @AuthenticationPrincipal UserDetails company){
        String email=company.getUsername();
        return companyService.approveApplicant(appli_id,email);
    }

    // reject the applicant by applicant ID
    @PutMapping("/rejectApplicant/{app_id}")
    public ResponseEntity rejectApplicant(@PathVariable Integer app_id, @AuthenticationPrincipal UserDetails company){
        String email=company.getUsername();
        return companyService.rejectApplicant(app_id,email);
    }

}
