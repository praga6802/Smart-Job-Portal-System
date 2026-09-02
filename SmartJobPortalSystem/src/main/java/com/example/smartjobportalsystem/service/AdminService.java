package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.Admin;
import com.example.smartjobportalsystem.entity.Candidate;
import com.example.smartjobportalsystem.entity.Company;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.repository.AdminRepository;
import com.example.smartjobportalsystem.repository.CandidateRepository;
import com.example.smartjobportalsystem.repository.CompanyRepository;
import com.example.smartjobportalsystem.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JWTService jwtService;

    // register admin
    public ResponseEntity<?> register(AdminRegisterDTO admin) {

        if (adminRepository.existsByEmail(admin.getEmail()) || usersRepository.existsByEmail(admin.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(), "Failure", "Email already exists!"));
        }

        if (adminRepository.existsByContact(admin.getContact())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(), "Failure", "Mobile Number already exists!"));
        }
        Users user = new Users();
        user.setEmail(admin.getEmail());
        user.setPassword(passwordEncoder.encode(admin.getPassword()));
        user.setRole("ADMIN");
        usersRepository.save(user);

        Admin admin1 = new Admin();
        admin1.setFirstname(admin.getFirstname());
        admin1.setLastname(admin.getLastname());
        admin1.setEmail(admin.getEmail());
        admin1.setContact(admin.getContact());
        admin1.setPassword(passwordEncoder.encode(admin.getPassword()));

        admin1.setUser(user);

        adminRepository.save(admin1);

        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(), "Success", "Registration Successfully"));
    }

    // update admin details
    @Transactional
    public ResponseEntity<?> updateAdmin(Integer adminId, AdminRegisterDTO admin) {
        List<String> updatedFields = new ArrayList<>();
        String newToken = null;

        Users user = usersRepository.findById(adminId).orElseThrow(() -> new NotFoundException("User not found!"));
        Admin admin1 = adminRepository.findByUserUserId(adminId).orElseThrow(() -> new NotFoundException("Admin not found!"));
        if (admin.getFirstname() != null && !admin.getFirstname().trim().isEmpty()) {
            admin1.setFirstname(admin.getFirstname());
            updatedFields.add("First name");
        }

        if (admin.getLastname() != null && !admin.getLastname().trim().isEmpty()) {
            admin1.setLastname(admin.getLastname());
            updatedFields.add("Last name");
        }

        if (admin.getEmail() != null && !admin.getEmail().trim().isEmpty()) {
            if (admin1.getEmail().equalsIgnoreCase(admin.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same Email"));
            }
            if (adminRepository.existsByEmail(admin.getEmail()) || usersRepository.existsByEmail(admin.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(), "Failure", "Email already taken!"));
            }
            admin1.setEmail(admin.getEmail());
            user.setEmail(admin.getEmail());
            usersRepository.save(user);
            updatedFields.add("Email");

            newToken = jwtService.generateToken(admin.getEmail());
        }
        if (admin.getContact() != null && !admin.getContact().trim().isEmpty()) {
            if (admin1.getContact().equals(admin.getContact())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same contact!"));
            }

            if (adminRepository.existsByContact(admin.getContact())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Contact already registered!"));
            }
            admin1.setContact(admin.getContact());
            updatedFields.add("Contact");
        }

        if (admin.getPassword() != null && !admin.getPassword().trim().isEmpty()) {
            if (admin1.getPassword().equals(admin.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same password!"));
            }
            admin1.setPassword(passwordEncoder.encode(admin.getPassword()));
            updatedFields.add("Password");
        }

        adminRepository.save(admin1);


        String message = "Company " + String.join(",", updatedFields) + " Updated Successfully!";
        if (newToken != null) {
            return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(), "Success", message, newToken));
        }
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(), "Success", message));
    }

    // get all admins
    public ResponseEntity<?> getAdmins() {
        List<AdminDTO> adminList = adminRepository.findAll().stream()
                .map(AdminDTO::new)
                .collect(Collectors.toList());

        if (adminList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(), "Failure", "No admins Found!"));
        }
        return ResponseEntity.ok(adminList);
    }

    // get all candidates
    public ResponseEntity<?> getCandidates() {
        List<CandidateDTO> candidateList = candidateRepository.findAll().stream().
                map(CandidateDTO::new).collect(Collectors.toList());
        if (candidateList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(), "Failure", "No Candidates Found!"));
        }
        return ResponseEntity.ok(candidateList);
    }

    // get all companies
    public ResponseEntity<?> getCompanies() {
        List<CompanyDTO> companyList = companyRepository.findAll().stream().
                map(CompanyDTO::new).collect(Collectors.toList());
        if (companyList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(), "Failure", "No Companies Found!"));
        }
        return ResponseEntity.ok(companyList);
    }

    // get admin by ID
    public ResponseEntity<?> getAdmin(Integer id) {
        try {
            Admin admin = adminRepository.findById(id).orElseThrow(() -> new NotFoundException("Admin not found with id: " + id));
            AdminDTO adminDTO = new AdminDTO(admin);
            return ResponseEntity.ok(adminDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(), "Failure", e.getMessage()));
        }
    }

    // get candidate by ID
    public ResponseEntity<?> getCandidate(Integer id) {
        try {
            Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new NotFoundException("Candidate not found with id: " + id));
            CandidateDTO candidateDTO = new CandidateDTO(candidate);
            return ResponseEntity.ok(candidateDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(), "Failure", e.getMessage()));
        }
    }

    // get company by ID
    public ResponseEntity<?> getCompany(Integer id) {
        try {
            Company company = companyRepository.findById(id).orElseThrow(() -> new NotFoundException("Company not found with id: " + id));
            CompanyDTO companyDTO = new CompanyDTO(company);
            return ResponseEntity.ok(companyDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(), "Failure", e.getMessage()));
        }
    }

    // delete candidate by ID
    public ResponseEntity<?> deleteCandidate(Integer candidateId) {
        try {
            Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new NotFoundException("Candidate not found with this id: " + candidateId));
            candidateRepository.deleteById(candidateId);
            return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(), "Success", "Candidate deleted successfully!"));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(),"Failure",e.getMessage()));
        }
    }

    // delete company by ID
    public ResponseEntity<?> deleteCompany(Integer companyId) {
        try {
            Company company = companyRepository.findById(companyId).orElseThrow(() -> new NotFoundException("Company not found with this id: " + companyId));
            companyRepository.deleteById(companyId);
            return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(), "Success", "Company deleted successfully!"));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(),"Failure",e.getMessage()));
        }
    }

    // Delete admin by ID
    public ResponseEntity<?> deleteAdmin(Integer adminId, Integer currentAdminId) {
        try {
            if (adminId.equals(currentAdminId)) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse(
                                LocalDateTime.now(),
                                "Failure",
                                "You cannot delete your own admin account"
                        ));
            }
            Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new NotFoundException("Admin not found with this id " + adminId));

            adminRepository.delete(admin);
            return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(), "Success", "Admin Deleted Successfully"));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(), "Failure", e.getMessage()));
        }
    }

    // delete all admins
    public ResponseEntity<?> deleteAdmins(Integer currentAdminId) {
            List<Admin> admins= adminRepository.findAll();
            if(admins.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(),"Failure","No Admins were found!"));
            }

            // remove the current logged in admin
            admins.removeIf(admin->admin.getUser().getUserId().equals(currentAdminId));

            // delete other admins
            adminRepository.deleteAll(admins);
            return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success","Admins deleted successfully"));
    }

    // delete all candidates
    public ResponseEntity<?> deleteCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();
        if(candidates.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(),"Failure","No Candidates were found!"));
        }
        candidateRepository.deleteAll();
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success","Candidates deleted successfully"));
    }

    // delete all companies
    public ResponseEntity<?> deleteCompanies() {
        List<Company> companies = companyRepository.findAll();
        if(companies.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(),"Failure","No Companies were found!"));
        }
        companyRepository.deleteAll();
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success","Companies deleted successfully"));
    }
}

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
