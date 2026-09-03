package com.example.smartjobportalsystem.controller;


import com.example.smartjobportalsystem.dto.CompanyRegisterDTO;
import com.example.smartjobportalsystem.dto.JobDTO;
import com.example.smartjobportalsystem.entity.Users;

import com.example.smartjobportalsystem.enums.ApplicationStatus;
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


    // get total applicants by company
    @GetMapping("/getAllApplicants")
    public ResponseEntity<?> getAllApplicants(@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.getAllApplicants(userDetails.getUserId());
    }


 // get applicants by job ID
    @GetMapping("/getApplicants/{jobId}")
    public ResponseEntity<?> getApplicantsByJob(@PathVariable Integer jobId, @AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.getApplicantsByJob(jobId,userDetails.getUserId());
    }



    // ------ JOB STATUS FEATURES ----
    @GetMapping("/approvedJobs")
    public ResponseEntity<?> getAllApprovedJobs(@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.getAllApprovedJobs(userDetails.getUserId());
    }

    @GetMapping("/rejectedJobs")
    public ResponseEntity<?> getAllRejectedJobs(@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.getAllRejectedJobs(userDetails.getUserId());
    }

    @GetMapping("/pendingJobs")
    public ResponseEntity<?> getAllPendingJobs(@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.getAllPendingJobs(userDetails.getUserId());
    }


    // ------- JOB ACTIVATE OR INACTIVATE FEATURES -------
    // activate the job
    @PutMapping("/jobs/activate/{jobId}")
    public ResponseEntity<?> activateJob(@PathVariable Integer jobId, @AuthenticationPrincipal MyUserDetails userDetails) {
        return companyService.activateJob(jobId, userDetails.getUserId());
    }

    // deactivate the job
    @PutMapping("/jobs/deactivate/{jobId}")
    public ResponseEntity<?> deactivateJob(@PathVariable Integer jobId, @AuthenticationPrincipal MyUserDetails userDetails) {
        return companyService.deactivateJob(jobId,userDetails.getUserId());
    }

    // get all activate jobs
    @GetMapping("/activateJobs")
    public ResponseEntity<?> getActivateJobs(@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.getActivateJobs(userDetails.getUserId());
    }

   // get all deactivate jobs
    @GetMapping("/deactivateJobs")
    public ResponseEntity<?> getDeactivateJobs(@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.getDeactivateJobs(userDetails.getUserId());
    }

    // ---- Application status features ---

    // Shortlist Application
    @PutMapping("/applications/shortlistApplication/{applicationId}")
    public ResponseEntity<?> shortlistApplication(@PathVariable Integer applicationId,@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.shortlistApplication(applicationId,userDetails.getUserId());
    }

    // Select Application
    @PutMapping("/applications/selectApplication/{applicationId}")
    public ResponseEntity<?> selectApplication(@PathVariable Integer applicationId,@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.selectApplication(applicationId,userDetails.getUserId());
    }

    // Reject Application
    @PutMapping("/applications/rejectApplication/{applicationId}")
    public ResponseEntity<?> rejectApplication(@PathVariable Integer applicationId,@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.rejectApplication(applicationId,userDetails.getUserId());
    }

    // get shortlisted applications
    @GetMapping("/shortlistedApplications")
    public ResponseEntity<?> getShortlistedApplications(@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.getShortlistedApplications(userDetails.getUserId());
    }

    // get selected applications
    @GetMapping("/selectedApplications")
    public ResponseEntity<?> getSelectedApplications(@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.getSelectedApplications(userDetails.getUserId());
    }

    // get rejected applications
    @GetMapping("/rejectedApplications")
    public ResponseEntity<?> getRejectedApplications(@AuthenticationPrincipal MyUserDetails userDetails){
        return companyService.getRejectedApplications(userDetails.getUserId());
    }




}
