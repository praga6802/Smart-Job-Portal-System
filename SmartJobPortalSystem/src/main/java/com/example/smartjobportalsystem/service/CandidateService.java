package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.*;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CompanyRepository companyRepository;


    // candidate registration
    public ResponseEntity<?> register(CandidateRegisterDTO candidate) {

        if(candidateRepository.existsByEmail(candidate.getEmail()) || usersRepository.existsByEmail(candidate.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(),"Failure","Email already exists!"));
        }

        if(candidateRepository.existsByContact(candidate.getContact())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(),"Failure","Mobile Number already exists!"));
        }

        Users user= new Users();
        user.setEmail(candidate.getEmail());
        user.setPassword(passwordEncoder.encode(candidate.getPassword()));
        user.setRole("CANDIDATE");
        usersRepository.save(user);


        Candidate c1= new Candidate();
        c1.setFirstname(candidate.getFirstname());
        c1.setLastname(candidate.getLastname());
        c1.setEmail(candidate.getEmail());
        c1.setContact(candidate.getContact());
        c1.setPassword(passwordEncoder.encode(candidate.getPassword()));
        c1.setDateOfBirth(candidate.getDob());
        c1.setExperience(candidate.getExperience());
        c1.setSkills(candidate.getSkills());
        c1.setUser(user);

        candidateRepository.save(c1);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Registration Successfully"));
    }


    // Update Candidate Details
    @Transactional
    public ResponseEntity<?> updateCandidate(Integer candidateId,CandidateRegisterDTO candidate) {
        List<String> updatedFields= new ArrayList<>();
        String newToken=null;

        Users user = usersRepository.findById(candidateId).orElseThrow(()-> new NotFoundException("User not found!"));
        Candidate cand = candidateRepository.findByUser_UserId(candidateId).orElseThrow(()-> new NotFoundException("Candidate not found!"));


        if(candidate.getFirstname()!=null && !candidate.getFirstname().trim().isEmpty()){
            cand.setFirstname(candidate.getFirstname());
            updatedFields.add("First name");
        }

        if(candidate.getLastname()!=null && !candidate.getLastname().trim().isEmpty()){
            cand.setLastname(candidate.getLastname());
            updatedFields.add("Last name");
        }

        if(candidate.getEmail()!=null && !candidate.getEmail().trim().isEmpty()){
            if(cand.getEmail().equals(candidate.getEmail())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same email"));
            }
            if(candidateRepository.existsByEmail(candidate.getEmail()) || usersRepository.existsByEmail(candidate.getEmail())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(),"Failure","Email already taken!"));
            }
            cand.setEmail(candidate.getEmail());
            user.setEmail(candidate.getEmail());
            usersRepository.save(user);
            updatedFields.add("Email");

            newToken=jwtService.generateToken(candidate.getEmail());
        }

        if(candidate.getContact()!=null && !candidate.getContact().trim().isEmpty()){
            if(cand.getContact().equals(candidate.getContact())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same contact!"));
            }

            if(candidateRepository.existsByContact(candidate.getContact())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Contact already registered!"));
            }
            cand.setContact(candidate.getContact());
            updatedFields.add("Contact");
        }

        if(candidate.getPassword()!=null && !candidate.getPassword().trim().isEmpty()){
            if(cand.getPassword().matches(candidate.getPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same password!"));
            }
            String encodedPassword = passwordEncoder.encode(candidate.getPassword());
            cand.setPassword(encodedPassword);
            user.setPassword(encodedPassword);
            usersRepository.save(user);

            updatedFields.add("Password");
        }

        if(candidate.getDob()!=null && !candidate.getDob().trim().isEmpty()){
            cand.setDateOfBirth(candidate.getDob());
            updatedFields.add("Date of Birth");
        }

        if(candidate.getExperience()!=null && !candidate.getExperience().trim().isEmpty()){
            cand.setExperience(candidate.getExperience());
            updatedFields.add("Experience");
        }

        if(candidate.getSkills()!=null && !candidate.getSkills().trim().isEmpty()){
            cand.setSkills(candidate.getSkills());
            updatedFields.add("Skills");
        }
        candidateRepository.save(cand);


        String message="Company "+String.join(",",updatedFields)+" Updated Successfully!";
        if(newToken!=null){
            return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success",message,newToken));
        }
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success",message));
    }


    // apply job
    public ResponseEntity<?> applyJob(Integer jobId, Integer candidateId) {

        Candidate candidate= candidateRepository.findByUser_UserId(candidateId).orElseThrow(()-> new NotFoundException("Candidate not found with id: "+candidateId));
        Job job = jobRepository.findById(jobId).orElseThrow(()-> new NotFoundException("Job not found with id: "+jobId));

        JobApplication jobApplication = new JobApplication();
        jobApplication.setCandidate(candidate);
        jobApplication.setJob(job);
        jobApplication.setDefaults();

        jobApplicationRepository.save(jobApplication);

        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success","Job Applied Successfully"));
    }


    // get job by company
    public ResponseEntity<?> getJobsByCompany(String companyName) {
        Company company = companyRepository.findByName(companyName).orElseThrow(()-> new NotFoundException("Company not found"));

        List<Job> companyJobList = jobRepository.findByCompany(company);
        if(companyJobList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(),"Failure","No jobs were found!"));
        }

        List<CompanyJobResponseDTO> jobList = companyJobList.stream().map(CompanyJobResponseDTO::new).toList();
        return ResponseEntity.ok(jobList);
    }

    // get all jobs
    public ResponseEntity<?> getJobs() {
        List<Job> jobs = jobRepository.findAll();
        if(jobs.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(),"Failure","No jobs were found!"));
        }
        List<JobResponseDTO> jobList = jobs.stream().map(JobResponseDTO::new).toList();

        return ResponseEntity.ok(jobList);
    }

    public ResponseEntity<?> viewApplicationStatus(Integer candidateId) {
        Candidate candidate= candidateRepository.findByUser_UserId(candidateId).orElseThrow(()-> new NotFoundException("Candidate not found with id: "+candidateId));

        List<JobApplication> jobApplications = jobApplicationRepository.findByCandidate(candidate);

        if(jobApplications.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(),"Failure","No Job applications were found!"));
        }

        List<ApplicationStatusDTO> statusList = jobApplications.stream().map(ApplicationStatusDTO::new).toList();
        return ResponseEntity.ok(statusList);
    }
}
