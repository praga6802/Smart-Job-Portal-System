package com.example.smartjobportalsystem.controller;


import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.pojo.MyUserDetails;
import com.example.smartjobportalsystem.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/auth/candidate")
public class CandidateController {



    @Autowired
    private CandidateService candidateService;

    // candidate registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CandidateRegisterDTO candidate){
        System.out.println("Password: "+ candidate.getPassword());
        return candidateService.register(candidate);
    }

    @PutMapping("/update")
    public  ResponseEntity<?> updateCandidate(@AuthenticationPrincipal MyUserDetails userDetails, @RequestBody CandidateRegisterDTO candidate){
        System.out.println("Log in candidate: "+userDetails.getUsername());
        return candidateService.updateCandidate(userDetails.getUserId(),candidate);
    }


    //  ---- JOB Application Features --------
    // apply job
    @PostMapping("/applyJob/{jobId}")
    public ResponseEntity<?> applyJob(@PathVariable Integer jobId, @AuthenticationPrincipal MyUserDetails userDetails){
        return candidateService.applyJob(jobId, userDetails.getUserId());
    }

    // get job by company
    @GetMapping("/get-job-by-company")
    public ResponseEntity<?> getJobsByCompany(@RequestParam String companyName) {
        return candidateService.getJobsByCompany(companyName);
    }


    // get all jobs
    @GetMapping("/get-jobs")
    public ResponseEntity<?> getJobs() {
        return candidateService.getJobs();
    }


    // view application status
    @GetMapping("/application-status")
    public ResponseEntity<?> viewApplicationStatus(@AuthenticationPrincipal MyUserDetails userDetails) {
        return candidateService.viewApplicationStatus(userDetails.getUserId());
    }


    // ----------------------- RESUME features ---------------------------
    // upload resume
    @PostMapping("/upload-resume")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal MyUserDetails candidate) throws IOException {
        return candidateService.uploadResume(file, candidate.getUserId());
    }

    //delete resume
    @DeleteMapping("/delete-resume")
    public ResponseEntity<?> deleteResume(@AuthenticationPrincipal MyUserDetails candidate) throws IOException {
        return candidateService.deleteResume(candidate.getUserId());
    }

    //view resume
    @GetMapping("/view-resume")
    public ResponseEntity<?> viewResume(@AuthenticationPrincipal MyUserDetails candidate) throws Exception{
        return candidateService.viewResume(candidate.getUserId());
    }

    //download resume
    @GetMapping("/download-resume")
    public ResponseEntity<?> downloadResume(@AuthenticationPrincipal MyUserDetails candidate) throws Exception{
        return candidateService.downloadResume(candidate.getUserId());
    }

    // ----------------------- EMAIL verification ---------------------------

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody CandidateRegistrationVerificationDTO candidate) {
        return candidateService.verifyOtp(candidate.getEmail(),candidate.getOtp());
    }

    @PostMapping("/verify-email-update")
    public ResponseEntity<?> verifyEmailUpdate(@RequestBody OTPRequestDTO otp, @AuthenticationPrincipal MyUserDetails userDetails){
        System.out.println(userDetails.getEmail()+" "+userDetails.getUsername());
        return candidateService.verifyEmailUpdate(otp.getOtp(),userDetails.getUserId());
    }

}
