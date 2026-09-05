package com.example.smartjobportalsystem.controller;

import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.pojo.MyUserDetails;
import com.example.smartjobportalsystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/get-admins")
    public ResponseEntity<?> getAdmins(){
        return adminService.getAdmins();
    }


    // get all Candidates
    @GetMapping("/get-candidates")
    public ResponseEntity<?> getCandidates(){
        return adminService.getCandidates();
    }


    // get all Companies
    @GetMapping("/get-companies")
    public ResponseEntity<?> getCompanies(){
        return adminService.getCompanies();
    }


    //get Admin details by ID
    @GetMapping("/getAdmin/{id}")
    public ResponseEntity<?> getAdmin(@PathVariable Integer id){
        return adminService.getAdmin(id);
    }


    //get Candidate details by ID
    @GetMapping("/getCandidate/{id}")
    public ResponseEntity<?> getCandidate(@PathVariable Integer id){
        return adminService.getCandidate(id);
    }

    //get Company details by ID
    @GetMapping("/getCompany/{id}")
    public ResponseEntity<?> getCompany(@PathVariable Integer id){
        return adminService.getCompany(id);
    }

    // Delete Candidate by ID
    @DeleteMapping("/deleteCandidate/{candidateId}")
    public ResponseEntity<?> deleteCandidate(@PathVariable Integer candidateId){
        return adminService.deleteCandidate(candidateId);
    }

    // Delete Company by ID
    @DeleteMapping("/deleteCompany/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable Integer companyId){
        return adminService.deleteCompany(companyId);
    }

    // Delete Admin By ID
    @DeleteMapping("/deleteAdmin/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer adminId, Authentication authentication){
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Integer currentAdminId=userDetails.getUserId();

        return adminService.deleteAdmin(adminId, currentAdminId);
    }


    // Delete all Admins
    @DeleteMapping("/delete-admins")
    public ResponseEntity<?> deleteAdmins(@AuthenticationPrincipal Users user){
        return adminService.deleteAdmins(user.getUserId());
    }

    // Delete all Candidates
    @DeleteMapping("/delete-candidates")
    public ResponseEntity<?> deleteCandidates(){
        return adminService.deleteCandidates();
    }

    // Delete all Companies
    @DeleteMapping("/delete-companies")
    public ResponseEntity<?> deleteCompanies(){
        return adminService.deleteCompanies();
    }

    // Pending Jobs
    @GetMapping("/pending-jobs")
    public ResponseEntity<?> getPendingJobs(){
        return adminService.getPendingJobs();
    }

    // Approved Jobs
    @GetMapping("/approved-jobs")
    public ResponseEntity<?> getApprovedJobs(){
        return adminService.getApprovedJobs();
    }

    // Rejected Jobs
    @GetMapping("/rejected-jobs")
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


   // --- ADMIN STATISTICS ---
    //Applications per job
    @GetMapping("/applications-per-job")
    public ResponseEntity<?> getApplicationsPerJob(){
        return adminService.getApplicationsPerJob();
    }

    //Applications per company
    @GetMapping("/applications-per-company")
    public ResponseEntity<?> getApplicationsPerCompany(){
        return adminService.getApplicationsPerCompany();
    }

    // total number of applications
    @GetMapping("/total-applications")
    public ResponseEntity<?> getTotalApplications(){
        return adminService.getTotalApplications();
    }

    // total number of companies
    @GetMapping("/total-companies")
    public ResponseEntity<?> getTotalCompanies(){
        return adminService.getTotalCompanies();
    }

    // total number of candidates
    @GetMapping("/total-candidates")
    public ResponseEntity<?> getTotalCandidates(){
        return adminService.getTotalCandidates();
    }

}
