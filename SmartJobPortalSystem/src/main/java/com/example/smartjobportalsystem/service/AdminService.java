package com.example.smartjobportalsystem.service;


import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.Job;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.exception.NameNotFoundException;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.repository.JobApplicationRepo;
import com.example.smartjobportalsystem.repository.JobRepo;
import com.example.smartjobportalsystem.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {


    @Autowired
    UsersRepo usersRepo;

    @Autowired
    JobApplicationRepo jobApplicationRepo;

    @Autowired
    JobRepo jobRepo;

    //delete user by id
    public ResponseEntity<?> deleteUserById(Integer id, String role){
        String user_role=role.substring(5);
         Users user=usersRepo.findById(id).orElseThrow(()-> new NotFoundException(user_role+" ID",id));

        if(!user.getRole().equalsIgnoreCase(role)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(),"Failed",user_role+" not found with ID "+id));
        }
        usersRepo.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success",user_role+" with ID "+id+" has been deleted Successfully"));
    }

    // get the user by user id
    public Users getUserById(Integer id){
       Users user=usersRepo.findById(id).orElseThrow(()-> new NotFoundException("Admin ID",id));
       return user;

    }

    // get all admins
    public List<UserDTO> getAllAdmins(){
        return usersRepo.getAllAdmins();
    }

    // get all users
    public List<UserDTO> getAllUsers(){
        return usersRepo.getAllUsers();
    }

    // get all companies
    public List<UserDTO> getAllCompanies(){
        return usersRepo.getAllCompanies();
    }


    // get the user by user id and role
    public ResponseEntity<?> getUserByIdAndRole(Integer id, String role){
        String user_role=role.substring(5);
        Users user=usersRepo.findById(id).orElseThrow(()-> new NotFoundException(user_role+" ID",id));
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success",role.substring(5)+" details fetched Successfully",
                new UserDTO(user.getUserId(),user.getUsername(),user.getEmail(), user.getRole().substring(5))));
    }

    //get user by username and role
    public ResponseEntity<?> getUserByUsernameAndRole(String username, String role) {
        String user_role=role.substring(5);
        Users user=usersRepo.findByUsername(username).orElseThrow(()-> new NameNotFoundException("Username",username));

        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success",user_role+" details fetched Successfully",
                new UserDTO(user.getUserId(),user.getUsername(),user.getEmail(), user.getRole().substring(5))));
    }

    //get application count per job
    public List<AppPerJobCount> getApplicationPerJob() {
        List<Object[]> results=jobApplicationRepo.countApplicationsPerJob();
        return results.stream().map(job-> new AppPerJobCount(
                (String)job[0],(Long)job[1]
        )).toList();
    }

    //get application count by company
    public List<AppPerCompanyCount> getApplicationPerCompany() {
        List<Object[]> results=jobApplicationRepo.countApplicationPerCompany();
        return results.stream().map(job-> new AppPerCompanyCount(
                (String)job[0],(Long)job[1]
        )).toList();

    }

    //approve job
    public ResponseEntity<?> approveJob(Integer id) {
        Job job=jobRepo.findById(id).orElseThrow(()-> new NotFoundException("Job ID",id));
        if(job.getStatus().equalsIgnoreCase("APPROVED"))
            return ResponseEntity.status(HttpStatus.FOUND).body(new ApiResponse(LocalDateTime.now(),"APPROVED","Job has been already APPROVED"));

        job.setStatus("APPROVED");
        jobRepo.save(job);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Job APPROVED for Job ID "+id));
    }

    //reject job
    public ResponseEntity<?> rejectJob(Integer id) {
        Job job=jobRepo.findById(id).orElseThrow(()-> new NotFoundException("Job ID",id));
        if(job.getStatus().equalsIgnoreCase("REJECTED"))
            return ResponseEntity.status(HttpStatus.FOUND).body(new ApiResponse(LocalDateTime.now(),"REJECTED","Job has been already REJECTED"));

        job.setStatus("REJECTED");
        jobRepo.save(job);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Failure","Job REJECTED for Job ID "+id));
    }


    // get all pending jobs
    public List<PendingJobDTO> getAllPendingJobs() {
        List<Job> pendingJobs=jobRepo.findByStatus("PENDING");
        return pendingJobs.stream().map(
                job-> new PendingJobDTO(job.getJobId(),job.getCompanyName(),job.getJobTitle(),job.getSkills(),job.getJobLocation(),job.getJobType(),job.getSalary(),job.getExperience())
        ).toList();
    }
}
