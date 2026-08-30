package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.Admin;
import com.example.smartjobportalsystem.entity.Candidate;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.repository.AdminRepository;
import com.example.smartjobportalsystem.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JWTService jwtService;

    public ResponseEntity<?> register(AdminRegisterDTO admin) {

        if(adminRepository.existsByEmail(admin.getEmail()) || usersRepository.existsByEmail(admin.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(),"Failure","Email already exists!"));
        }

        if(adminRepository.existsByContact(admin.getContact())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(),"Failure","Mobile Number already exists!"));
        }
        Users user= new Users();
        user.setEmail(admin.getEmail());
        user.setPassword(passwordEncoder.encode(admin.getPassword()));
        user.setRole("ROLE_ADMIN");
        usersRepository.save(user);

        Admin admin1= new Admin();
        admin1.setFirstname(admin.getFirstname());
        admin1.setLastname(admin.getLastname());
        admin1.setEmail(admin.getEmail());
        admin1.setContact(admin.getContact());
        admin1.setPassword(passwordEncoder.encode(admin.getPassword()));

        admin1.setUser(user);

        adminRepository.save(admin1);

        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Registration Successfully"));
    }

    public ResponseEntity<?> updateAdmin(String email, AdminRegisterDTO admin) {
        List<String> updatedFields= new ArrayList<>();
        String newToken =null;
        Admin admin1 = adminRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Admin not found with email "+email));
        Users user = usersRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found with email "+email));
        if(admin.getFirstname()!=null && !admin.getFirstname().trim().isEmpty()){
            admin1.setFirstname(admin.getFirstname());
            updatedFields.add("First name");
        }
        if(admin.getLastname()!=null && !admin.getLastname().trim().isEmpty()){
            admin1.setLastname(admin.getLastname());
            updatedFields.add("Last name");
        }
        if(admin.getEmail()!=null && !admin.getEmail().trim().isEmpty()){
            if(admin.getEmail().equalsIgnoreCase(email)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same Email"));
            }
            if(adminRepository.existsByEmail(admin.getEmail()) || usersRepository.existsByEmail(admin.getEmail())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(),"Failure","Email already taken!"));
            }
            admin1.setEmail(admin.getEmail());
            user.setEmail(admin.getEmail());
            usersRepository.save(user);
            updatedFields.add("Email");

            newToken=jwtService.generateToken(admin.getEmail());
        }
        if(admin.getContact()!=null && !admin.getContact().trim().isEmpty()){
            if(admin1.getContact().equals(admin.getContact())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same contact!"));
            }

            if(adminRepository.existsByContact(admin.getContact())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Contact already registered!"));
            }
            admin1.setContact(admin.getContact());
            updatedFields.add("Contact");
        }
        if(admin.getPassword()!=null && !admin.getPassword().trim().isEmpty()){
            if(admin1.getPassword().equals(admin.getPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same password!"));
            }
            admin1.setPassword(passwordEncoder.encode(admin.getPassword()));
            updatedFields.add("Password");
        }

        adminRepository.save(admin1);


        String message="Company "+String.join(",",updatedFields)+" Updated Successfully!";
        if(newToken!=null){
            return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success",message,newToken));
        }
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success",message));
    }
}
//
//
//    @Autowired
//    UsersRepo usersRepo;
//
//    @Autowired
//    JobApplicationRepo jobApplicationRepo;
//
//    @Autowired
//    JobRepo jobRepo;
//
//    //delete user by id
//    public ResponseEntity<?> deleteUserById(Integer id, String role){
//        String user_role=role.substring(5);
//         Users user=usersRepo.findById(id).orElseThrow(()-> new NotFoundException(user_role+" ID",id));
//
//        if(!user.getRole().equalsIgnoreCase(role)){
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(),"Failed",user_role+" not found with ID "+id));
//        }
//        usersRepo.deleteById(id);
//        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success",user_role+" with ID "+id+" has been deleted Successfully"));
//    }
//
//    // get the user by user id
//    public Users getUserById(Integer id){
//       Users user=usersRepo.findById(id).orElseThrow(()-> new NotFoundException("Admin ID",id));
//       return user;
//
//    }
//
//    // get all admins
//    public List<UserDTO> getAllAdmins(){
//        return usersRepo.getAllAdmins();
//    }
//
//    // get all users
//    public List<UserDTO> getAllUsers(){
//        return usersRepo.getAllUsers();
//    }
//
//    // get all companies
//    public List<UserDTO> getAllCompanies(){
//        return usersRepo.getAllCompanies();
//    }
//
//
//    // get the user by user id and role
//    public ResponseEntity<?> getUserByIdAndRole(Integer id, String role){
//        String user_role=role.substring(5);
//        Users user=usersRepo.findById(id).orElseThrow(()-> new NotFoundException(user_role+" ID",id));
//        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success",role.substring(5)+" details fetched Successfully",
//                new UserDTO(user.getUserId(),user.getUsername(),user.getEmail(), user.getRole().substring(5),user.getMobNumber())));
//    }
//
//    //get user by username and role
//    public ResponseEntity<?> getUserByUsernameAndRole(String username, String role) {
//        String user_role=role.substring(5);
//        Users user=usersRepo.findByUsername(username).orElseThrow(()-> new NameNotFoundException("Username",username));
//
//        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success",user_role+" details fetched Successfully",
//                new UserDTO(user.getUserId(),user.getUsername(),user.getEmail(), user.getRole().substring(5),user.getMobNumber())));
//    }
//
//    //get application count per job
//    public List<AppPerJobCount> getApplicationPerJob() {
//        List<Object[]> results=jobApplicationRepo.countApplicationsPerJob();
//        return results.stream().map(job-> new AppPerJobCount(
//                (String)job[0],(Long)job[1]
//        )).toList();
//    }
//
//    //get application count by company
//    public List<AppPerCompanyCount> getApplicationPerCompany() {
//        List<Object[]> results=jobApplicationRepo.countApplicationPerCompany();
//        return results.stream().map(job-> new AppPerCompanyCount(
//                (String)job[0],(Long)job[1]
//        )).toList();
//
//    }
//
//    //approve job
//    public ResponseEntity<?> approveJob(Integer id) {
//        Job job=jobRepo.findById(id).orElseThrow(()-> new NotFoundException("Job ID",id));
//        if(job.getStatus().equalsIgnoreCase("APPROVED"))
//            return ResponseEntity.status(HttpStatus.FOUND).body(new ApiResponse(LocalDateTime.now(),"APPROVED","Job has been already APPROVED"));
//
//        job.setStatus("APPROVED");
//        jobRepo.save(job);
//        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Job APPROVED for Job ID "+id));
//    }
//
//    //reject job
//    public ResponseEntity<?> rejectJob(Integer id) {
//        Job job=jobRepo.findById(id).orElseThrow(()-> new NotFoundException("Job ID",id));
//        if(job.getStatus().equalsIgnoreCase("REJECTED"))
//            return ResponseEntity.status(HttpStatus.FOUND).body(new ApiResponse(LocalDateTime.now(),"REJECTED","Job has been already REJECTED"));
//
//        job.setStatus("REJECTED");
//        jobRepo.save(job);
//        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Failure","Job REJECTED for Job ID "+id));
//    }
//
//
//    // get all pending jobs
//    public List<PendingJobDTO> getAllPendingJobs() {
//        List<Job> pendingJobs=jobRepo.findByStatus("PENDING");
//        return pendingJobs.stream().map(
//                job-> new PendingJobDTO(job.getJobId(),job.getCompanyName(),job.getJobTitle(),job.getSkills(),job.getJobLocation(),job.getJobType(),job.getSalary(),job.getExperience())
//        ).toList();
//    }
//}
