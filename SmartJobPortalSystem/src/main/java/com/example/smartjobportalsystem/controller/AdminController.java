package com.example.smartjobportalsystem.controller;

import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.service.AdminService;
import com.example.smartjobportalsystem.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("auth/admin")
public class AdminController {


    @Autowired
    AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AdminRegisterDTO admin){
        return adminService.register(admin);
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateAdmin(Authentication authentication, @RequestBody AdminRegisterDTO admin){
        String email=authentication.getName();
        return adminService.updateAdmin(email,admin);
    }

    // ADMIN

    //delete admin by id
//    @DeleteMapping("/delete/admin/{id}")
//    public ResponseEntity<?> deleteAdminById(@PathVariable Integer id, @AuthenticationPrincipal UserDetails loggedInUser){
//
//        Users user=adminService.getUserById(id);
//        String emailLogin=loggedInUser.getUsername();
//
//        if(user.getEmail().equals(emailLogin)){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
//                    body(new ApiResponse(LocalDateTime.now(),"Failure","Admin cannot delete their own account"));
//        }
//        return adminService.deleteUserById(id,"ROLE_ADMIN");
//    }

    //get all admins
//    @GetMapping("/getAllAdmins")
//    public List<UserDTO> getAllAdmins(){
//        return adminService.getAllAdmins();
//    }
//
//    //get admin by id
//    @GetMapping("/getAdminByID/{id}")
//    public ResponseEntity<?> getAdminByID(@PathVariable Integer id){
//        return adminService.getUserByIdAndRole(id,"ROLE_ADMIN");
//    }
//
//    // get admin by username
//    @GetMapping("/getAdminByUsername/{username}")
//    public ResponseEntity<?> getAdminByUsername(@PathVariable String username){
//        return adminService.getUserByUsernameAndRole(username,"ROLE_ADMIN");
//    }
//
//    //USER
//    //delete user by id
//    @DeleteMapping("/delete/user/{id}")
//    public ResponseEntity<?> deleteUserById(@PathVariable Integer id){
//        return adminService.deleteUserById(id,"ROLE_USER");
//    }
//
//    //get all users
//    @GetMapping("/getAllUsers")
//    public List<UserDTO> getAllUsers(){
//        return adminService.getAllUsers();
//    }

//    //get user details by user id
//    @GetMapping("/getUserByID/{id}")
//    public ResponseEntity<?> getUserByID(@PathVariable Integer id){
//        return adminService.getUserByIdAndRole(id,"ROLE_USER");
//    }
//
//    //get user details by username
//    @GetMapping("/getUserByUsername/{username}")
//    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
//        System.out.println(username);
//        return adminService.getUserByUsernameAndRole(username,"ROLE_USER");
//    }
//
//
//    // COMPANY
//    //delete company by id
//    @DeleteMapping("/delete/company/{id}")
//    public ResponseEntity<?> deleteCompanyById(@PathVariable Integer id){
//        return adminService.deleteUserById(id,"ROLE_COMPANY");
//    }
//
//
//    //get all companies
//    @GetMapping("/getAllCompanies")
//    public List<UserDTO> getAllCompanies(){
//        return adminService.getAllCompanies();
//    }
//
//
//    //get company details by id
//    @GetMapping("/getCompanyByID/{id}")
//    public ResponseEntity<?> getCompanyByID(@PathVariable Integer id){
//        return adminService.getUserByIdAndRole(id,"ROLE_COMPANY");
//    }
//
//
//    //get company details by username
//    @GetMapping("/getCompanyByUsername/{username}")
//    public ResponseEntity<?> getCompanyByUsername(@PathVariable String username){
//        return adminService.getUserByUsernameAndRole(username,"ROLE_COMPANY");
//    }
//
//
//    //approve job by id
//    @PutMapping("/approveJob/{id}")
//    public ResponseEntity<?> approveJob(@PathVariable Integer id){
//        return adminService.approveJob(id);
//    }
//
//
//    //reject job by id
//    @PutMapping("/rejectJob/{id}")
//    public ResponseEntity<?> rejectJob(@PathVariable Integer id){
//        return adminService.rejectJob(id);
//    }
//
//
//    //all pendingJobs
//    @GetMapping("/pendingJobs")
//    public List<PendingJobDTO> getAllPendingJobs(){
//        return adminService.getAllPendingJobs();
//    }
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
