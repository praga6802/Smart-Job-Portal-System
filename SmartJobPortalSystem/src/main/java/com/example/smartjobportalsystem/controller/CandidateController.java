package com.example.smartjobportalsystem.controller;


import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.pojo.MyUserDetails;
import com.example.smartjobportalsystem.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/candidate")
public class CandidateController {



    @Autowired
    private CandidateService candidateService;

    // candidate registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CandidateRegisterDTO candidate){
        return candidateService.register(candidate);
    }

    @PutMapping("/update")
    public  ResponseEntity<?> updateCandidate(@AuthenticationPrincipal MyUserDetails userDetails, @RequestBody CandidateRegisterDTO candidate){
        return candidateService.updateCandidate(userDetails.getUserId(),candidate);
    }


    //  ---- JOB Application Features --------
    // apply job
    @PostMapping("/applyJob/{jobId}")
    public ResponseEntity<?> applyJob(@PathVariable Integer jobId, @AuthenticationPrincipal MyUserDetails userDetails){
        System.out.println("Candidate ID: "+userDetails.getUserId());
        return candidateService.applyJob(jobId, userDetails.getUserId());
    }

    // get job by company
    @GetMapping("/jobs")
    public ResponseEntity<?> getJobsByCompany(@RequestParam String companyName) {
        return candidateService.getJobsByCompany(companyName);
    }


    // get all jobs
    @GetMapping("/viewAllJobs")
    public ResponseEntity<?> getJobs() {
        return candidateService.getJobs();
    }


    // view application status
    @GetMapping("/application-status")
    public ResponseEntity<?> viewApplicationStatus(@AuthenticationPrincipal MyUserDetails userDetails) {
        return candidateService.viewApplicationStatus(userDetails.getUserId());
    }


//
//
//    // ----------------------- EMAIL verification ---------------------------
//    @PostMapping("/verifyEmail")
//    public ResponseEntity<?> verifyEmailAndSendCode(@RequestBody EmailReqDTO emailDTO, @AuthenticationPrincipal UserDetails user) {
//        String logEmail = user.getUsername();
//        String mail = emailDTO.getEmail();
//        return userService.verifyEmailAndSendCode(logEmail, mail);
//    }
//
//    @PostMapping("/verifyEmailCode")
//    public ResponseEntity<?> verifyEmailCode(@RequestBody EmailVerificationDTO verify, @AuthenticationPrincipal UserDetails user) {
//        return userService.verifyEmailCode(user, verify.getCode());
//    }
//
//
//    // ----------------------- RESUME features ---------------------------
//    // upload resume
//    @PostMapping("/uploadResume")
//    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails user) throws IOException {
//        String email = user.getUsername();
//        return userService.uploadResume(file, email);
//    }
//
//    //delete resume
//    @DeleteMapping("/deleteResume")
//    public ResponseEntity<?> deleteResume(@AuthenticationPrincipal UserDetails user) throws IOException {
//        String email=user.getUsername();
//        return userService.deleteResume(email);
//    }
//
//  //view resume
//    @GetMapping("/viewResume")
//    public ResponseEntity<?> viewResume(@AuthenticationPrincipal UserDetails user) throws Exception{
//        String email=user.getUsername();
//        return userService.viewResume(email);
//    }
//
//    //download resume
//    @GetMapping("/downloadResume")
//    public ResponseEntity<?> downloadResume(@AuthenticationPrincipal UserDetails user) throws Exception{
//        String email=user.getUsername();
//        return userService.downloadResume(email);
//    }

}
