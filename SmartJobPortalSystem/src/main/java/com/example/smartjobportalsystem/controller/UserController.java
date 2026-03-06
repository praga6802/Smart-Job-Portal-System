package com.example.smartjobportalsystem.controller;


import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Autowired
    UserService userService;

    // apply for the job
    @PostMapping("/applyJob/{id}")
    public ResponseEntity<?> applyJob(@PathVariable Integer id, Principal principal){
        return userService.applyJob(id,principal.getName());
    }

    // view application status for job
    @GetMapping("/viewApplicationStatus")
    public ResponseEntity<?> viewApplicationStatus(@AuthenticationPrincipal UserDetails applicant){
        String email=applicant.getUsername();
        return userService.viewApplicationStatus(email);
    }

    // find the job by company name
    @GetMapping("/viewJobsByCompany/{companyName}")
    public ResponseEntity<?> viewJobsByCompany(@PathVariable String companyName){
        List<JobDTO> companyJobs = userService.viewJobsByCompany(companyName);
        return ResponseEntity.ok(companyJobs);
    }

    @GetMapping("/viewJobs")
    public ResponseEntity<List<JobDTO>> getAllJobs(){
        List<JobDTO> allJobs= userService.viewAllJobs();
        return ResponseEntity.ok(allJobs);
    }

    //upload new resume
//    @PostMapping("/uploadResume")
//    public ResponseEntity<?> uploadResume(@RequestParam MultipartFile file, @AuthenticationPrincipal UserDetails user) throws IOException {
//        String email=user.getUsername();
//        return userService.uploadResume(file,email);
//    }
//
//    //delete resume for the user
//    @DeleteMapping("/deleteResume")
//    public ResponseEntity<?> deleteResume(@AuthenticationPrincipal UserDetails user) throws IOException {
//        String email=user.getUsername();
//        return userService.deleteResume(email);
//    }
//
//    //view resume
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

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmailAndSendCode(@RequestBody EmailReqDTO emailDTO, @AuthenticationPrincipal UserDetails user){
        String logEmail=user.getUsername();
        String mail=emailDTO.getEmail();
        return userService.verifyEmailAndSendCode(logEmail,mail);
    }

    @PostMapping("/verifyEmailCode")
    public ResponseEntity<?> verifyEmailCode(@RequestBody EmailVerificationDTO verify, @AuthenticationPrincipal UserDetails user){
        return userService.verifyEmailCode(user, verify.getCode());
    }
}
