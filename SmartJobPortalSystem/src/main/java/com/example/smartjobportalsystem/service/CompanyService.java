package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.dto.*;

import com.example.smartjobportalsystem.entity.Company;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {


        @Autowired
        private CompanyRepository companyRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private UsersRepository usersRepository;

        @Autowired
        private JWTService jwtService;


        // registration service
        public ResponseEntity<?> register(CompanyRegisterDTO company) {

            if(companyRepository.existsByEmail(company.getEmail()) || usersRepository.existsByEmail(company.getEmail())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(),"Failure","Email already exists!"));
            }

            if(companyRepository.existsByContact(company.getContact())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(),"Failure","Mobile Number already exists!"));
            }

            if(companyRepository.existsByUrl(company.getUrl())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(),"Failure","Company URL already exists!"));
            }

            if(companyRepository.existsByGst(company.getGst())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(LocalDateTime.now(),"Failure","GST Number already exists!"));
            }

            Users user= new Users();
            user.setEmail(company.getEmail());
            user.setPassword(passwordEncoder.encode(company.getPassword()));
            user.setRole("ROLE_COMPANY");
            usersRepository.save(user);

            Company c1= new Company();
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

            return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Registration Successfully"));
        }

    public ResponseEntity<?> updateCompany(String email,CompanyRegisterDTO company) {
        List<String> updatedFields= new ArrayList<>();
        String newToken =null;
        Company comp = companyRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Company not found with email "+email));
        Users user = usersRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found with email "+email));

        if(company.getName()!=null && !company.getName().trim().isEmpty()){
            comp.setName(company.getName());
            updatedFields.add("Name");
        }

        if(company.getEmail()!=null && !company.getEmail().trim().isEmpty()){
            if(company.getEmail().equalsIgnoreCase(email)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same Email"));
            }
            if(companyRepository.existsByEmail(company.getEmail())){
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new LoginResponse(LocalDateTime.now(),"Failure","Email already taken!"));
            }
            comp.setEmail(company.getEmail());
            user.setEmail(company.getEmail());
            usersRepository.save(user);
            updatedFields.add("Email");
            newToken=jwtService.generateToken(company.getEmail());
        }
        if(company.getContact()!=null && !company.getContact().trim().isEmpty()){
            if(comp.getContact().equals(comp.getContact())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same contact!"));
            }

            if(companyRepository.existsByContact(company.getContact())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Contact already registered!"));
            }
            comp.setContact(company.getContact());
            updatedFields.add("Contact");
        }
        if(company.getPassword()!=null && !company.getPassword().trim().isEmpty()){
            if(comp.getPassword().equals(company.getPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same password!"));
            }
            comp.setPassword(passwordEncoder.encode(company.getPassword()));
            updatedFields.add("Password");
        }

        if(company.getUrl()!=null && !company.getUrl().trim().isEmpty()){
            if(comp.getUrl().equals(comp.getUrl())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same URL!"));
            }

            if(companyRepository.existsByUrl(company.getUrl())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","URL already registered!"));
            }
            comp.setUrl(company.getUrl());
            updatedFields.add("URL");
        }

        if(company.getGst()!=null && !company.getGst().trim().isEmpty()){
            if(comp.getGst().equals(comp.getGst())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same GST!"));
            }

            if(companyRepository.existsByGst(company.getGst())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","GST already registered!"));
            }
            comp.setGst(company.getGst());
            updatedFields.add("GST");
        }

        if(company.getDescription()!=null && !company.getDescription().trim().isEmpty()){
            if(comp.getDescription().equals(company.getDescription())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same description!"));
            }
            comp.setDescription(company.getDescription());
            updatedFields.add("Description");
        }

        if(company.getSize()!=null && !company.getSize().trim().isEmpty()){
            if(comp.getSize().equals(company.getSize())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same size!"));
            }
            comp.setSize(company.getSize());
            updatedFields.add("Size");
        }

        if(company.getType()!=null && !company.getType().trim().isEmpty()){
            if(comp.getType().equals(company.getType())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same type!"));
            }
            comp.setType(company.getType());
            updatedFields.add("Type");
        }

        if(company.getLocation()!=null && !company.getLocation().trim().isEmpty()){
            if(comp.getLocation().equals(company.getLocation())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same location!"));
            }
            comp.setLocation(company.getLocation());
            updatedFields.add("Location");
        }


        companyRepository.save(comp);

        if(updatedFields.isEmpty()){
            return ResponseEntity.ok(
                    new ApiResponse(
                            LocalDateTime.now(),
                            "Success",
                            "No fields were updated!"
                    )
            );
        }

        String message="Company "+String.join(",",updatedFields)+" Updated Successfully!";
        if(newToken!=null){
            return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success",message,newToken));
        }
        return ResponseEntity.ok(new LoginResponse(LocalDateTime.now(),"Success",message));
    }
}

    // add a new job
//    public ResponseEntity<?> postJob(JobDTO job, String email){
//        Users company=usersRepo.findByEmail(email).orElseThrow(()-> new UnAuthorizedException("Company Email",email));
//        Job j= new Job();
//        j.setJobTitle(job.getTitle());
//        j.setJobDescription(job.getDescription());
//        j.setSalary(job.getSalary());
//        j.setExperience(job.getExperience());
//        j.setJobLocation(job.getLocation());
//        j.setJobType(job.getType());
//        j.setCompany(company);
//        j.setCompanyName(company.getUsername());
//        j.setPostedDate(LocalDateTime.now());
//        j.setSkills(job.getSkills());
//        j.setStatus("PENDING");
//        jobRepo.save(j);
//        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","New Job has been posted Successfully"));
//    }


    // update job info
//    public ResponseEntity<?> updateJob(Integer id, JobDTO job, String email) {
//        Users company = usersRepo.findByEmail(email).orElseThrow(() -> new UnAuthorizedException("Company Email", email));
//
//        Job exisitingJob = jobRepo.findByJobIdAndCompanyUserId(id, company.getUserId())
//                .orElseThrow(() -> new NameNotFoundException("Company with ", "Job ID " + id));
//
//        if (job.getTitle() != null && !job.getTitle().isBlank()) {
//            exisitingJob.setJobTitle(job.getTitle());
//        }
//
//        if (job.getSkills() != null && !job.getSkills().isEmpty()) {
//            exisitingJob.setSkills(job.getSkills());
//        }
//        if (job.getDescription() != null && !job.getDescription().isBlank()) {
//            exisitingJob.setJobDescription(job.getDescription());
//
//        }
//        if (job.getSalary() != null && job.getSalary() != 0) {
//            exisitingJob.setSalary(job.getSalary());
//
//        }
//        if (job.getLocation() != null && !job.getLocation().isBlank()) {
//            exisitingJob.setJobLocation(job.getLocation());
//
//        }
//        if (job.getExperience() != null && !job.getExperience().isBlank()) {
//            exisitingJob.setExperience(job.getExperience());
//        }
//        if (job.getType() != null && !job.getType().isBlank()) {
//            exisitingJob.setJobType(job.getType());
//        }
//
//        jobRepo.save(exisitingJob);
//        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(), "Success", "Job Details Updated Successfully"));
//    }


    // delete job
//    public ResponseEntity<?> deleteJob(Integer id, String email) {
//        Users company = usersRepo.findByEmail(email).orElseThrow(() -> new UnAuthorizedException("Company Email", email));
//
//        Job exisitingJob = jobRepo.findByJobIdAndCompanyUserId(id, company.getUserId())
//                .orElseThrow(() -> new NameNotFoundException("Company with ", "Job ID " + id));
//
//        jobRepo.deleteById(id);
//        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(), "Success", "Job Details Deleted Successfully"));
//    }


    //get all jobs for respective company
//    public List<JobDetailResponse> getAllJobs(String email) {
//        Users company = usersRepo.findByEmail(email).orElseThrow(() -> new UnAuthorizedException("Email ID", email));
//
//        List<Job> jobs = jobRepo.findByCompany(company);
//        List<JobDetailResponse> jobDetailResponse = jobs.stream()
//                .map(j -> new JobDetailResponse(
//                        j.getJobId(), j.getJobTitle(), j.getJobDescription(),
//                        j.getSkills(), j.getJobLocation(), j.getJobType(),
//                        j.getSalary(), j.getExperience())
//                ).toList();
//        return jobDetailResponse;
//    }


    //get particular job from company
//    public ResponseEntity<?> getJobById(Integer id, String email) {
//        Users company = usersRepo.findByEmail(email).orElseThrow(() -> new UnAuthorizedException("Email ID", email));
//
//        Job job = jobRepo.findByJobIdAndCompanyUserId(id, company.getUserId()).orElseThrow(() -> new NotFoundException("Job ID", id));
//        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(), "Success", "Job with ID " + id + " has been fetched successfully",
//                new JobDetailResponse(
//                        job.getJobId(), job.getJobTitle(), job.getJobDescription(),
//                        job.getSkills(), job.getJobLocation(), job.getJobType(),
//                        job.getSalary(), job.getExperience())
//        ));
//    }


    // get applicants for the job id
//    public ResponseEntity<?> getApplicantsByJobId(Integer jobId, String email) {
//        Job job = jobRepo.findById(jobId).orElseThrow(() -> new NotFoundException("Job ID", jobId));
//
//        if(!job.getCompany().getEmail().equals(email)){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse(LocalDateTime.now(),"Not Found","Job ID Not found"));
//        }
//
//        //fetch job details by job id
//        List<JobApplication> jobApplication = jobApplicationRepo.findByJob(job);
//
//        //if no users were applied to this job
//        if (jobApplication.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).
//                    body(new ApiResponse(LocalDateTime.now(), "Not Found", "No applicants have applied for this job yet."));
//        }
//
//        //if users were applied to this job
//        List<ApplicantsDTO> applicantResponse = jobApplication.stream().
//                map(a -> new ApplicantsDTO(
//                        a.getApplicant().getUsername(),
//                        a.getApplicant().getEmail(),
//                        a.getAppliedAt()
//                )).toList();
//
//        return ResponseEntity.ok(applicantResponse);



    // get all applicants for the company
//    public ResponseEntity<?> getAllApplicants(String email) {
//
//        Users company=usersRepo.findByEmail(email).orElseThrow(()-> new UnAuthorizedException("Company",email));
//
//        List<Job> companyJobs=jobRepo.findByCompany(company);
//
//        if(companyJobs.isEmpty()){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).
//                    body(new ApiResponse(LocalDateTime.now(),"Not Found","You haven't posted any jobs yet."));
//        }
//
//        List<JobApplication> jobApplications=jobApplicationRepo.findByJobIn(companyJobs);
//
//        if(jobApplications.isEmpty()){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).
//                    body(new ApiResponse(LocalDateTime.now(),"Not Found","No applicants found for your job postings.\""));
//        }
//
//        //if some user applied to a particular job in this company
//        List<ApplicantsDTO> applicantResponse=jobApplications.stream().
//                map(a-> new ApplicantsDTO(
//                        a.getApplicationId(),
//                        a.getApplicant().getUserId(),
//                        a.getJob().getJobId(),
//                        a.getJob().getJobTitle(),
//                        a.getApplicant().getUsername(),
//                        a.getApplicant().getEmail(),
//                        a.getStatus(),
//                        a.getAppliedAt()
//                )).toList();
//        return ResponseEntity.ok(applicantResponse);
//    }


    // approve applicant
//    public ResponseEntity approveApplicant(Integer applicationId, String email) {
//        //validate applicant with appId
//        JobApplication application = jobApplicationRepo.findById(applicationId).orElseThrow(() -> new NotFoundException("Application ID", applicationId));
//
//        Users company = usersRepo.findByEmail(email).orElseThrow(() -> new UnAuthorizedException("Company Email ID", email));
//        Job job=application.getJob();
//
//        if (!job.getCompany().getEmail().equals(email)) {
//            return ResponseEntity.ok
//                    (new ApiResponse(LocalDateTime.now(), "Failure", "You are not allowed to approve applicants for this job."));
//        }
//        if(application.getStatus().equalsIgnoreCase("APPROVED"))
//            return ResponseEntity.status(HttpStatus.FOUND).body(new ApiResponse(LocalDateTime.now(),"APPROVED","Applicant has been already APPROVED"));
//
//        application.setStatus("APPROVED");
//        jobApplicationRepo.save(application);
//        subject="Congratulations! You are Selected";
//        body="Dear " + application.getApplicant().getUsername() + ",\n\n" +
//                "We are pleased to inform you that you have been SELECTED for the role "+application.getJob().getJobTitle()+" at " +application.getJob().getCompanyName()+".\n"
//                +"Our HR team will contact you soon.\n\n" +
//                "Regards,\n"+application.getJob().getCompany().getUsername()+" Team.";
//
//        emailService.sendEmail(new EmailDTO(application.getApplicant().getEmail(),subject,body));
//        return ResponseEntity.ok
//                (new ApiResponse(LocalDateTime.now(), "Success", "Approved successfully for Applicant ID:" + application.getApplicant().getUserId()));
//    }
//
//
//    //reject applicant
//    public ResponseEntity rejectApplicant(Integer applicationId, String email) {
//        JobApplication application = jobApplicationRepo.findById(applicationId).orElseThrow(() -> new NotFoundException("Application ID", applicationId));
//
//        Users company = usersRepo.findByEmail(email).orElseThrow(() -> new UnAuthorizedException("Company Email ID", email));
//        Job job=application.getJob();
//
//        if (!job.getCompany().getEmail().equals(email)) {
//            return ResponseEntity.ok
//                    (new ApiResponse(LocalDateTime.now(), "Failure", "You are not allowed to reject applicants for this job."));
//        }
//        if(application.getStatus().equalsIgnoreCase("REJECTED"))
//            return ResponseEntity.status(HttpStatus.FOUND).body(new ApiResponse(LocalDateTime.now(),"REJECTED","Applicant has been already REJECTED"));
//
//        application.setStatus("REJECTED");
//        jobApplicationRepo.save(application);
//        subject="Application Status Update";
//        body = "Dear " + application.getApplicant().getUsername() + ",\n\n" +
//                "Thank you for showing your interest towards our job posting!\n" +
//                "We regret to inform you that you are not selected for the role you applied for "+application.getJob().getJobTitle()+"."+"\n\n" +
//                "Best wishes for your future!\n"+application.getJob().getCompany().getUsername()+" Team.";
//
//        emailService.sendEmail(new EmailDTO(application.getApplicant().getEmail(),subject,body));
//
//        return ResponseEntity.ok
//                (new ApiResponse(LocalDateTime.now(), "Success", "Rejected successfully for Applicant ID:" + application.getApplicant().getUserId()));
//    }




