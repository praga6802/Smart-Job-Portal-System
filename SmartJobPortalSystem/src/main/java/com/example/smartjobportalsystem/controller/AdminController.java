package com.example.smartjobportalsystem.controller;

import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.pojo.MyUserDetails;
import com.example.smartjobportalsystem.service.AdminService;
import com.example.smartjobportalsystem.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("auth/admin")
public class AdminController {


    @Autowired
    AdminService adminService;

    // register admin
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AdminRegisterDTO admin){
        return adminService.register(admin);
    }


    // update admin
    @PutMapping("/update")
    public ResponseEntity<?> updateAdmin(@AuthenticationPrincipal MyUserDetails userDetails, @RequestBody AdminRegisterDTO admin){
        return adminService.updateAdmin(userDetails.getUserId(),admin);
    }


    // get all Admins
    @GetMapping("/getAdmins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdmins(){
        return adminService.getAdmins();
    }


    // get all Candidates
    @GetMapping("/getCandidates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCandidates(){
        return adminService.getCandidates();
    }


    // get all Companies
    @GetMapping("/getCompanies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCompanies(){
        return adminService.getCompanies();
    }


    //get Admin details by ID
    @GetMapping("/getAdmin/{id}")
    @PreAuthorize("role('ADMIN')")
    public ResponseEntity<?> getAdmin(@PathVariable Integer id){

        return adminService.getAdmin(id);
    }


    //get Candidate details by ID
    @GetMapping("/getCandidate/{id}")
    @PreAuthorize("role('ADMIN')")
    public ResponseEntity<?> getCandidate(@PathVariable Integer id){

        return adminService.getCandidate(id);
    }

    //get Company details by ID
    @GetMapping("/getCompany/{id}")
    @PreAuthorize("role('ADMIN')")
    public ResponseEntity<?> getCompany(@PathVariable Integer id){

        return adminService.getCompany(id);
    }

    // Delete Candidate by ID
    @DeleteMapping("/deleteCandidate/{candidateId}")
    @PreAuthorize("role('ADMIN')")
    public ResponseEntity<?> deleteCandidate(@PathVariable Integer candidateId){
        return adminService.deleteCandidate(candidateId);
    }

    // Delete Company by ID
    @DeleteMapping("/deleteCompany/{companyId}")
    @PreAuthorize("role('ADMIN')")
    public ResponseEntity<?> deleteCompany(@PathVariable Integer companyId){
        return adminService.deleteCompany(companyId);
    }

    // Delete Admin By ID
    @DeleteMapping("/deleteAdmin/{adminId}")
    @PreAuthorize("role('ADMIN')")
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer adminId, Authentication authentication){
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Integer currentAdminId=userDetails.getUserId();

        return adminService.deleteAdmin(adminId, currentAdminId);
    }


    // Delete all Admins
    @DeleteMapping("/deleteAdmins")
    @PreAuthorize("role('ADMIN')")
    public ResponseEntity<?> deleteAdmins(@AuthenticationPrincipal Users user){
        return adminService.deleteAdmins(user.getUserId());
    }

    // Delete all Candidates
    @DeleteMapping("/deleteCandidates")
    @PreAuthorize("role('ADMIN')")
    public ResponseEntity<?> deleteCandidates(){
        return adminService.deleteCandidates();
    }

    // Delete all Companies
    @DeleteMapping("/deleteCompanies")
    @PreAuthorize("role('ADMIN')")
    public ResponseEntity<?> deleteCompanies(){
        return adminService.deleteCompanies();
    }


    // Pending Jobs
    @GetMapping("/pendingJobs")
    public ResponseEntity<?> getPendingJobs(){
        return adminService.getPendingJobs();
    }

    // Approved Jobs
    @GetMapping("/approvedJobs")
    public ResponseEntity<?> getApprovedJobs(){
        return adminService.getApprovedJobs();
    }

    // Rejected Jobs
    @GetMapping("/rejectedJobs")
    public ResponseEntity<?> getRejectedJobs(){
        return adminService.getRejectedJobs();
    }


    // Approve Job
    @PutMapping("/approveJob/{jobId}")
    public ResponseEntity<?> approveJob(@PathVariable Integer jobId){
        return adminService.approveJob(jobId);
    }


    //reject job by id
   @PutMapping("/rejectJob/{jobId}")
   public ResponseEntity<?> rejectJob(@PathVariable Integer jobId){
       return adminService.rejectJob(jobId);
   }

//
//
//    //get the number of applicants per job
//    @GetMapping("/application-per-job")
//    public ResponseEntity<?> getApplicationPerJob(){
//        return ResponseEntity.ok(adminService.getApplicationPerJob());
//    }
//
//
//    //get the number of applicants per company
//    @GetMapping("/application-per-company")
//    public ResponseEntity<?> getApplicationPerCompany(){
//        return ResponseEntity.ok(adminService.getApplicationPerCompany());
//    }

}
