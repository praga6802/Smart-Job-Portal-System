package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.Company;
import com.example.smartjobportalsystem.entity.Job;
import com.example.smartjobportalsystem.entity.JobApplication;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.enums.ApplicationStatus;
import com.example.smartjobportalsystem.enums.JobStatus;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.exception.UnAuthorizedException;
import com.example.smartjobportalsystem.repository.CompanyRepository;
import com.example.smartjobportalsystem.repository.JobApplicationRepository;
import com.example.smartjobportalsystem.repository.JobRepository;
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
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

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

    // registration service
    public ResponseEntity<?> register(CompanyRegisterDTO company) {

        if (companyRepository.existsByEmail(company.getEmail()) || usersRepository.existsByEmail(company.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(), "Failure", "Email already exists!"));
        }

        if (companyRepository.existsByContact(company.getContact())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(), "Failure", "Mobile Number already exists!"));
        }

        if (companyRepository.existsByUrl(company.getUrl())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(), "Failure", "Company URL already exists!"));
        }

        if (companyRepository.existsByGst(company.getGst())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(), "Failure", "GST Number already exists!"));
        }

        Users user = new Users();
        user.setEmail(company.getEmail());
        user.setPassword(passwordEncoder.encode(company.getPassword()));
        user.setRole("COMPANY");
        usersRepository.save(user);

        Company c1 = new Company();
        c1.setName(company.getName());
        c1.setDescription(company.getDescription());
        c1.setEmail(company.getEmail());
        c1.setContact(company.getContact());
        c1.setPassword(passwordEncoder.encode(company.getPassword()));
        c1.setUrl(company.getUrl());
        c1.setSize(company.getSize());
        c1.setType(company.getType());
        c1.setLocation(company.getLocation());
        c1.setGst(company.getGst());
        c1.setUser(user);

        companyRepository.save(c1);

        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(), "Success", "Registration Successfully"));
    }

    // update company details
    @Transactional
    public ResponseEntity<?> updateCompany(Integer userId, CompanyRegisterDTO company) {


        List<String> updatedFields = new ArrayList<>();
        String newToken = null;

        Users user = usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        Company comp = companyRepository.findByUser_UserId(userId).orElseThrow(() -> new NotFoundException("Company not found!"));


        if (company.getName() != null && !company.getName().trim().isEmpty()) {
            comp.setName(company.getName());
            updatedFields.add("Name");
        }

        if (company.getEmail() != null && !company.getEmail().trim().isEmpty()) {
            if (company.getEmail().equals(comp.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same Email"));
            }

            if (companyRepository.existsByEmail(company.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(), "Failure", "Email already taken!"));
            }
            comp.setEmail(company.getEmail());
            user.setEmail(company.getEmail());
            usersRepository.save(user);
            updatedFields.add("Email");
            newToken = jwtService.generateToken(company.getEmail());
        }

        if (company.getContact() != null && !company.getContact().trim().isEmpty()) {
            if (comp.getContact().equals(company.getContact())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same contact!"));
            }

            if (companyRepository.existsByContact(company.getContact())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Contact already registered!"));
            }
            comp.setContact(company.getContact());
            updatedFields.add("Contact");
        }

        if (company.getPassword() != null && !company.getPassword().trim().isEmpty()) {
            if (comp.getPassword().matches(company.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same password!"));
            }
            String encodedPassword = passwordEncoder.encode(company.getPassword());
            comp.setPassword(encodedPassword);
            user.setPassword(encodedPassword);
            usersRepository.save(user);

            updatedFields.add("Password");
        }

        if (company.getUrl() != null && !company.getUrl().trim().isEmpty()) {
            if (comp.getUrl().equals(company.getUrl())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same URL!"));
            }

            if (companyRepository.existsByUrl(company.getUrl())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "URL already registered!"));
            }
            comp.setUrl(company.getUrl());
            updatedFields.add("URL");
        }

        if (company.getGst() != null && !company.getGst().trim().isEmpty()) {
            if (comp.getGst().equals(company.getGst())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same GST!"));
            }

            if (companyRepository.existsByGst(company.getGst())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "GST already registered!"));
            }
            comp.setGst(company.getGst());
            updatedFields.add("GST");
        }

        if (company.getDescription() != null && !company.getDescription().trim().isEmpty()) {
            if (comp.getDescription().equals(company.getDescription())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same description!"));
            }
            comp.setDescription(company.getDescription());
            updatedFields.add("Description");
        }

        if (company.getSize() != null && !company.getSize().trim().isEmpty()) {
            if (comp.getSize().equals(company.getSize())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same size!"));
            }
            comp.setSize(company.getSize());
            updatedFields.add("Size");
        }

        if (company.getType() != null && !company.getType().trim().isEmpty()) {
            if (comp.getType().equals(company.getType())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same type!"));
            }
            comp.setType(company.getType());
            updatedFields.add("Type");
        }

        if (company.getLocation() != null && !company.getLocation().trim().isEmpty()) {
            if (comp.getLocation().equals(company.getLocation())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(), "Failure", "Do not enter same location!"));
            }
            comp.setLocation(company.getLocation());
            updatedFields.add("Location");
        }


        companyRepository.save(comp);

        if (updatedFields.isEmpty()) {
            return ResponseEntity.ok(
                    new ApiResponse(
                            LocalDateTime.now(),
                            "Success",
                            "No fields were updated!"
                    )
            );
        }

        String message = "Company " + String.join(",", updatedFields) + " Updated Successfully!";
        if (newToken != null) {
            return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(), "Success", message, newToken));
        }
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(), "Success", message));
    }

    // post new job
    public ResponseEntity<?> postJob(JobDTO job, Integer companyId) {
        Company company = companyRepository.findByUser_UserId(companyId).orElseThrow(() -> new NotFoundException("Company not found!"));
        Job j = new Job();
        j.setTitle(job.getTitle());
        j.setDescription(job.getDescription());
        j.setSalary(job.getSalary());
        j.setExperience(job.getExperience());
        j.setLocation(job.getLocation());
        j.setType(job.getType());
        j.setSkills(job.getSkills());

        j.setCompany(company);

        jobRepository.save(j);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(), "Success", "New Job has been posted Successfully"));
    }

    // update job details
    public ResponseEntity<?> updateJob(Integer jobId, JobDTO jobDTO, Integer companyId) {
        Company company = companyRepository.findByUser_UserId(companyId).orElseThrow(() -> new NotFoundException("Company not found!"));

        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found!"));

        List<String> updatedDetails = new ArrayList<>();

        if (job.getCompany().getCompanyId().equals(companyId)) {
            throw new UnAuthorizedException(company.getEmail(), jobDTO.getTitle());
        }

        if (jobDTO.getTitle() != null && !jobDTO.getTitle().isEmpty()) {
            job.setTitle(jobDTO.getTitle());
            updatedDetails.add("Title");
        }

        if (jobDTO.getDescription() != null && !jobDTO.getDescription().isEmpty()) {
            job.setDescription(jobDTO.getDescription());
            updatedDetails.add("Description");
        }

        if (jobDTO.getSkills() != null && !jobDTO.getSkills().isEmpty()) {
            job.setSkills(jobDTO.getSkills());
            updatedDetails.add("Skills");
        }

        if (jobDTO.getSalary() != null && jobDTO.getSalary() != 0.00) {
            job.setSalary(jobDTO.getSalary());
            updatedDetails.add("Salary");
        }

        if (jobDTO.getExperience() != null && !jobDTO.getExperience().isEmpty()) {
            job.setExperience(jobDTO.getExperience());
            updatedDetails.add("Experience");
        }

        if (jobDTO.getLocation() != null && !jobDTO.getLocation().isEmpty()) {
            job.setLocation(jobDTO.getLocation());
            updatedDetails.add("Location");
        }

        if (jobDTO.getType() != null && !jobDTO.getType().isEmpty()) {
            job.setType(jobDTO.getType());
            updatedDetails.add("Type");
        }

        jobRepository.save(job);

        String message = "Job " + String.join(",", updatedDetails) + " updated Successfully!";

        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(), "Success", message));
    }

    // delete job by ID
    @Transactional
    public ResponseEntity<?> deleteJob(Integer jobId, Integer companyId) {
        Company company = companyRepository.findByUser_UserId(companyId).orElseThrow(() -> new NotFoundException("Company not found with ID: " + companyId));

        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found with ID: " + jobId));


        if (!job.getCompany().getCompanyId().equals(company.getCompanyId())) {
            throw new UnAuthorizedException(company.getEmail(), job.getTitle());
        }

        jobRepository.delete(job);
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(), "Success", "Job Deleted Successfully!"));

    }

    // delete all jobs
    public ResponseEntity<?> deleteJobs(Integer companyId) {
        Company company = companyRepository.findByUser_UserId(companyId).orElseThrow(() -> new NotFoundException("Company not found!"));

        List<Job> jobs = jobRepository.findByCompany(company);

        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(), "Failure", "No jobs were found!"));
        }
        jobRepository.deleteAll(jobs);
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(), "Success", "All jobs deleted Successfully"));
    }

    // get all jobs
    public ResponseEntity<?> getJobs(Integer companyId) {
        Company company = companyRepository.findByUser_UserId(companyId).orElseThrow(() -> new NotFoundException("Company not found"));

        List<Job> jobs = jobRepository.findByCompany(company);
        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(), "Failure", "No jobs were found!"));
        }

        List<JobResponseDTO> jobList = jobs.stream().map(JobResponseDTO::new).toList();

        return ResponseEntity.ok(jobList);
    }

    // get job by ID
    public ResponseEntity<?> getJobById(Integer jobId, Integer companyId) {
        Company company = companyRepository.findByUser_UserId(companyId).orElseThrow(() -> new NotFoundException("Company not found with id: " + companyId));

        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found with id: " + jobId));

        if (!job.getCompany().getCompanyId().equals(company.getCompanyId())) {
            throw new UnAuthorizedException(company.getEmail(), job.getTitle());
        }

        return ResponseEntity.ok(new JobResponseDTO(job));
    }

    //get status of job posted by company
    public ResponseEntity<?> getAdminJobStatus(Integer companyId) {
        Company company = companyRepository.findByUser_UserId(companyId).orElseThrow(() -> new NotFoundException("Company not found with ID: " + companyId));

        List<Job> companyJobs = jobRepository.findByCompany(company);
        if (companyJobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(), "Failure", "No jobs were found!"));
        }

        List<CompanyJobStatusDTO> statusList = companyJobs.stream().map(CompanyJobStatusDTO::new).toList();
        return ResponseEntity.ok(statusList);
    }

    // get all applicants
    public ResponseEntity<?> getAllApplicants(Integer companyId) {
        Company company = companyRepository.findByUser_UserId(companyId).orElseThrow(() -> new NotFoundException("Company not found with ID: " + companyId));
        List<JobApplication> jobApplications = jobApplicationRepository.findByJob_Company(company);

        if (jobApplications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(), "Failure", "No Applicants were found!"));
        }

        List<JobApplicantsResponseDTO> applicantsList = jobApplications.stream().map(application -> {
            return new JobApplicantsResponseDTO(application.getAppliedAt(), application.getCandidate().getFirstname(),
                    application.getCandidate().getLastname(), application.getCandidate().getEmail(),
                    application.getCandidate().getContact(), application.getCandidate().getSkills(), application.getCandidate().getExperience(),
                    application.getJob().getJobId(), application.getJob().getTitle());
        }).toList();

        return ResponseEntity.ok(applicantsList);
    }


    // get applicants by job ID
    public ResponseEntity<?> getApplicantsByJob(Integer jobId, Integer companyId) {
        Company company = companyRepository.findByUser_UserId(companyId).orElseThrow(() -> new NotFoundException("Company not found with ID: " + companyId));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found with ID: !" + jobId));

        if (!job.getCompany().getCompanyId().equals(company.getCompanyId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(), "Failure", "You are not authorized to view this job's applicants!"));
        }

        List<JobApplication> jobApplications = jobApplicationRepository.findByJob(job);

        if (jobApplications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LocalDateTime.now(), "Failure", "No Applicants were found!"));
        }
        List<JobApplicantsResponseDTO> applicantsList = jobApplications.stream().map(application -> {
            return new JobApplicantsResponseDTO(
                    application.getAppliedAt(),
                    application.getCandidate().getFirstname(),
                    application.getCandidate().getLastname(),
                    application.getCandidate().getEmail(),
                    application.getCandidate().getContact(),
                    application.getCandidate().getSkills(),
                    application.getCandidate().getExperience());
        }).toList();

        return ResponseEntity.ok(applicantsList);


    }


    // update job as activate
    public ResponseEntity<?> activateJob(Integer jobId, Integer companyId) {
        Company company = companyRepository.findByUser_UserId(companyId).orElseThrow(() -> new NotFoundException("Company not found with this ID: " + companyId));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found with this ID: " + jobId));

        if (job.getCompany().getCompanyId().equals(company.getCompanyId())) {
            if (job.getStatus() != JobStatus.APPROVED) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(),
                        "Failure", "Only approved jobs can be activated!"));
            }

            if (job.isActive()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(), "Failure", "Already it is activated!"));
            }
            job.setActive(true);
            jobRepository.save(job);
            return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(), "Success", "Job Activated Successfully"));
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse(LocalDateTime.now(),"Failure","You are not allowed to set active status!"));
        }
    }

    // update job as deactivate
    public ResponseEntity<?> deactivateJob(Integer jobId, Integer companyId) {
        Company company = companyRepository.findByUser_UserId(companyId).orElseThrow(() -> new NotFoundException("Company not found with this ID: " + companyId));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found with this ID: " + jobId));

        if (job.getCompany().getCompanyId().equals(company.getCompanyId())) {
            if (job.getStatus() != JobStatus.APPROVED) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(),
                        "Failure", "Only approved jobs can be deactivated!"));
            }

            if (!job.isActive()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(), "Failure", "Already it is deactivated!"));
            }
            job.setActive(false);
            jobRepository.save(job);
            return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(), "Success", "Job Deactivated Successfully"));
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse(LocalDateTime.now(),"Failure","You are not allowed to set active status!"));
        }
    }

    // shortlist application
    public ResponseEntity<?> shortlistApplication(Integer applicationId, Integer companyId) {
        JobApplication jobApplication =jobApplicationRepository.findById(applicationId).orElseThrow(()-> new NotFoundException("Application not found with ID: "+applicationId));
        Company company =companyRepository.findByUser_UserId(companyId).orElseThrow(()-> new NotFoundException("Company not found with ID: ",companyId));

        if(!jobApplication.getJob().getCompany().getCompanyId().equals(company.getCompanyId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse(LocalDateTime.now(),"Failure","You cannot SHORTLIST this application"));
        }

        if(jobApplication.getStatus()!=ApplicationStatus.APPLIED){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse(LocalDateTime.now(),"Failure","Only with Status:Applied can be shortlisted!"));
        }

        jobApplication.setStatus(ApplicationStatus.SHORTLISTED);
        jobApplicationRepository.save(jobApplication);
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success","Application with ID: "+applicationId+" has been SHORTLISTED Successfully!"));
    }


    // select application
    public ResponseEntity<?> selectApplication(Integer applicationId, Integer companyId) {
        JobApplication jobApplication =jobApplicationRepository.findById(applicationId).orElseThrow(()-> new NotFoundException("Application not found with ID: "+applicationId));
        Company company =companyRepository.findByUser_UserId(companyId).orElseThrow(()-> new NotFoundException("Company not found with ID: ",companyId));

        if(!jobApplication.getJob().getCompany().getCompanyId().equals(company.getCompanyId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse(LocalDateTime.now(),"Failure","You cannot SELECT this application"));
        }

        if(jobApplication.getStatus()!=ApplicationStatus.SHORTLISTED){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse(LocalDateTime.now(),"Failure","Only with Status:Shortlisted can be Selected!"));
        }

        jobApplication.setStatus(ApplicationStatus.SELECTED);
        jobApplicationRepository.save(jobApplication);
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success","Application with ID: "+applicationId+" has been SELECTED Successfully!"));
    }

    // reject application
    public ResponseEntity<?> rejectApplication(Integer applicationId, Integer companyId) {
        JobApplication jobApplication =jobApplicationRepository.findById(applicationId).orElseThrow(()-> new NotFoundException("Application not found with ID: "+applicationId));
        Company company =companyRepository.findByUser_UserId(companyId).orElseThrow(()-> new NotFoundException("Company not found with ID: ",companyId));

        if(!jobApplication.getJob().getCompany().getCompanyId().equals(company.getCompanyId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse(LocalDateTime.now(),"Failure","You cannot REJECT this application"));
        }

        if(jobApplication.getStatus()==ApplicationStatus.SELECTED || jobApplication.getStatus()==ApplicationStatus.REJECTED){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new LoginResponse(LocalDateTime.now(),"Failure","Application cannot be rejected when status is: "+jobApplication.getStatus()));
        }

        jobApplication.setStatus(ApplicationStatus.REJECTED);
        jobApplicationRepository.save(jobApplication);
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success","Application with ID: "+applicationId+" has been REJECTED Successfully!"));
    }

    public ResponseEntity<?> getAllApprovedJobs(Integer companyId) {
        Company company= companyRepository.findByUser_UserId(companyId).orElseThrow(()-> new NotFoundException("Company not found with this ID: "+companyId));

        List<Job> companyJobs= jobRepository.findByCompanyAndStatus(company, JobStatus.APPROVED);

        if(companyJobs.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(),"Failure","No APPROVED jobs were found!"));
        }

        List<JobStatusDTO> jobList = companyJobs.stream().map(JobStatusDTO::new).toList();
        return ResponseEntity.ok(jobList);
    }

    public ResponseEntity<?> getAllRejectedJobs(Integer companyId) {
        Company company= companyRepository.findByUser_UserId(companyId).orElseThrow(()-> new NotFoundException("Company not found with this ID: "+companyId));

        List<Job> companyJobs= jobRepository.findByCompanyAndStatus(company, JobStatus.REJECTED);

        if(companyJobs.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(),"Failure","No REJECTED jobs were found!"));
        }

        List<JobStatusDTO> jobList = companyJobs.stream().map(JobStatusDTO::new).toList();
        return ResponseEntity.ok(jobList);
    }

    public ResponseEntity<?> getAllPendingJobs(Integer companyId) {
        Company company= companyRepository.findByUser_UserId(companyId).orElseThrow(()-> new NotFoundException("Company not found with this ID: "+companyId));

        List<Job> companyJobs= jobRepository.findByCompanyAndStatus(company, JobStatus.PENDING);

        if(companyJobs.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(),"Failure","No PENDING jobs were found!"));
        }

        List<JobStatusDTO> jobList = companyJobs.stream().map(JobStatusDTO::new).toList();
        return ResponseEntity.ok(jobList);
    }


}

