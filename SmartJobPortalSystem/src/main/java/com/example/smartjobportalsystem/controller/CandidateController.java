package com.example.smartjobportalsystem.controller;


import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.pojo.MyUserDetails;
import com.example.smartjobportalsystem.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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


    //  ---- JOB FEATURES --------
//    // apply
//    @PostMapping("/applyJob/{id}")
//    public ResponseEntity<?> applyJob(@PathVariable Integer id, Principal principal) {
//        return userService.applyJob(id, principal.getName());
//    }
//
//    // view application status
//    @GetMapping("/application-status")
//    public ResponseEntity<?> viewApplicationStatus(@AuthenticationPrincipal UserDetails applicant) {
//        String email = applicant.getUsername();
//        return userService.viewApplicationStatus(email);
//    }
//
//    // find job by company
//    @GetMapping("/viewJobsByCompany/{companyName}")
//    public ResponseEntity<?> viewJobsByCompany(@PathVariable String companyName) {
//        List<JobDTO> companyJobs = userService.viewJobsByCompany(companyName);
//        return ResponseEntity.ok(companyJobs);
//    }
//
//    // get all jobs
//    @GetMapping("/viewAllJobs")
//    public ResponseEntity<List<JobDTO>> getAllJobs() {
//        List<JobDTO> allJobs = userService.viewAllJobs();
//        return ResponseEntity.ok(allJobs);
//    }
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
