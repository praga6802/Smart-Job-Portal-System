package com.example.smartjobportalsystem.controller;


import com.example.smartjobportalsystem.dto.CompanyRegisterDTO;
import com.example.smartjobportalsystem.dto.JobDTO;
import com.example.smartjobportalsystem.entity.Users;

import com.example.smartjobportalsystem.pojo.MyUserDetails;
import com.example.smartjobportalsystem.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    // company registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CompanyRegisterDTO company){
        return companyService.register(company);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCompany(@AuthenticationPrincipal MyUserDetails user, @RequestBody CompanyRegisterDTO company){
        return companyService.updateCompany(user.getUserId(),company);
    }

    // -------- JOB FEATURES ------------
    // post job
    @PostMapping("/postJob")
    public ResponseEntity<?> postJob(@RequestBody JobDTO job, @AuthenticationPrincipal MyUserDetails user){
        return companyService.postJob(job,user.getUserId());
    }

    //update job by id
    @PutMapping("/updateJob/{jobId}")
    public ResponseEntity<?> updateJob(@PathVariable Integer jobId,@RequestBody JobDTO job, @AuthenticationPrincipal MyUserDetails company){
        return companyService.updateJob(jobId,job,company.getUserId());
    }


    // delete job by ID
    @DeleteMapping("/deleteJob/{jobid}")
    public ResponseEntity<?> deleteJob(@PathVariable Integer jobId, @AuthenticationPrincipal MyUserDetails company){
        return companyService.deleteJob(jobId,company.getUserId());
    }


    // delete all jobs
    @DeleteMapping("/deleteJobs")
    public ResponseEntity<?> deleteJobs(@AuthenticationPrincipal MyUserDetails company){
        return companyService.deleteJobs(company.getUserId());
    }


    //get all jobs
    @GetMapping("/getAllJobs")
    public ResponseEntity<?> getJobs(@AuthenticationPrincipal MyUserDetails company){
        return companyService.getJobs(company.getUserId());
    }

    //get job by id
    @GetMapping("/getJobById/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable Integer jobId, @AuthenticationPrincipal MyUserDetails company){
        return companyService.getJobById(jobId,company.getUserId());
    }

    @GetMapping("/job-status")
    public ResponseEntity<?> viewJobStatus(@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.viewJobStatus(userDetails.getUserId());
    }


//    // get applicants for the particular job
//    @GetMapping("viewApplicants/{jobId}")
//    public ResponseEntity<?> getApplicantsByJob(@PathVariable Integer jobId, @AuthenticationPrincipal UserDetails company){
//        String email=company.getUsername();
//        return companyService.getApplicantsByJob(jobId,email);
//    }


    // get all applicants by company
    @GetMapping("/viewApplicants")
    public ResponseEntity<?> getAllApplicants(@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.getAllApplicants(userDetails.getUserId());
    }
//
//    // approve the applicant by applicant ID
//    @PutMapping("/approveApplicant/{appli_id}")
//    public ResponseEntity approveApplicant(@PathVariable Integer appli_id, @AuthenticationPrincipal UserDetails company){
//        String email=company.getUsername();
//        return companyService.approveApplicant(appli_id,email);
//    }
//
//    // reject the applicant by applicant ID
//    @PutMapping("/rejectApplicant/{app_id}")
//    public ResponseEntity rejectApplicant(@PathVariable Integer app_id, @AuthenticationPrincipal UserDetails company){
//        String email=company.getUsername();
//        return companyService.rejectApplicant(app_id,email);
//    }

}
