package com.example.SpringSecurity.service;

import com.example.SpringSecurity.dto.*;
import com.example.SpringSecurity.entity.Job;
import com.example.SpringSecurity.entity.JobApplication;
import com.example.SpringSecurity.entity.Users;
import com.example.SpringSecurity.exception.NameNotFoundException;
import com.example.SpringSecurity.exception.NotFoundException;
import com.example.SpringSecurity.exception.UnAuthorizedException;
import com.example.SpringSecurity.repository.JobApplicationRepo;
import com.example.SpringSecurity.repository.JobRepo;
import com.example.SpringSecurity.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyService {

    @Autowired
    JobRepo jobRepo;

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    JobApplicationRepo jobApplicationRepo;

    @Autowired
    EmailService emailService;
    private String subject;
    private String body;


    // add a new job
    public ResponseEntity<?> postJob(JobDTO job, String email){
        Users company=usersRepo.findByEmail(email).orElseThrow(()-> new UnAuthorizedException("Company Email",email));
        Job j= new Job();
        j.setJobTitle(job.getTitle());
        j.setJobDescription(job.getDescription());
        j.setSalary(job.getSalary());
        j.setExperience(job.getExperience());
        j.setJobLocation(job.getLocation());
        j.setJobType(job.getType());
        j.setCompany(company);
        j.setCompanyName(company.getUsername());
        j.setPostedDate(LocalDateTime.now());
        j.setSkills(job.getSkills());
        j.setStatus("PENDING");
        jobRepo.save(j);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","New Job has been posted Successfully"));
    }


    // update job info
    public ResponseEntity<?> updateJob(Integer id,JobDTO job, String email) {
        Users company=usersRepo.findByEmail(email).orElseThrow(()-> new UnAuthorizedException("Company Email",email));

        Job exisitingJob=jobRepo.findByJobIdAndCompanyUserId(id,company.getUserId())
                .orElseThrow(()-> new NameNotFoundException("Company with ","Job ID "+id));

        if(job.getTitle()!=null && !job.getTitle().isBlank()){
            exisitingJob.setJobTitle(job.getTitle());
        }

        if(job.getSkills()!=null && !job.getSkills().isEmpty()){
            exisitingJob.setSkills(job.getSkills());
        }
        if(job.getDescription()!=null && !job.getDescription().isBlank()){
            exisitingJob.setJobDescription(job.getDescription());

        }if(job.getSalary()!=null && job.getSalary()!=0){
            exisitingJob.setSalary(job.getSalary());

        }if(job.getLocation()!=null && !job.getLocation().isBlank()){
            exisitingJob.setJobLocation(job.getLocation());

        }if(job.getExperience()!=null && !job.getExperience().isBlank()){
            exisitingJob.setExperience(job.getExperience());
        }
        if(job.getType()!=null && !job.getType().isBlank()){
            exisitingJob.setJobType(job.getType());
        }

        jobRepo.save(exisitingJob);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Job Details Updated Successfully"));
    }


    // delete job
    public ResponseEntity<?> deleteJob(Integer id, String email) {
        Users company=usersRepo.findByEmail(email).orElseThrow(()-> new UnAuthorizedException("Company Email",email));

        Job exisitingJob=jobRepo.findByJobIdAndCompanyUserId(id,company.getUserId())
                .orElseThrow(()-> new NameNotFoundException("Company with ","Job ID "+id));

        jobRepo.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Job Details Deleted Successfully"));
    }


    //get all jobs for respective company
    public List<JobDetailResponse> getAllJobs(String email) {
        Users company=usersRepo.findByEmail(email).orElseThrow(()-> new UnAuthorizedException("Email ID",email));

       List<Job> jobs=jobRepo.findByCompany(company);
       List<JobDetailResponse> jobDetailResponse=jobs.stream()
               .map(j-> new JobDetailResponse(
                       j.getJobId(),j.getJobTitle(),j.getJobDescription(),
                       j.getSkills(),j.getJobLocation(),j.getJobType(),
                       j.getSalary(),j.getExperience())
       ).toList();
       return jobDetailResponse;
    }


    //get particular job from company
    public ResponseEntity<?> getJobById(Integer id, String email) {
        Users company=usersRepo.findByEmail(email).orElseThrow(()-> new UnAuthorizedException("Email ID",email));

       Job job= jobRepo.findByJobIdAndCompanyUserId(id,company.getUserId()).orElseThrow(()-> new NotFoundException("Job ID",id));
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Job with ID "+id+" has been fetched successfully",
                new JobDetailResponse(
                        job.getJobId(),job.getJobTitle(),job.getJobDescription(),
                        job.getSkills(),job.getJobLocation(),job.getJobType(),
                        job.getSalary(),job.getExperience())
        ));
    }


    // get applicants for the job id
    public ResponseEntity<?> getApplicantsByJobId(Integer jobId, String email) {
        Job job = jobRepo.findById(jobId).orElseThrow(() -> new NotFoundException("Job ID", jobId));

        if(!job.getCompany().getEmail().equals(email)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(LocalDateTime.now(),"Not Found","Job ID Not found"));
        }

        //fetch job details by job id
        List<JobApplication> jobApplication = jobApplicationRepo.findByJob(job);

        //if no users were applied to this job
        if (jobApplication.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ApiResponse(LocalDateTime.now(), "Not Found", "No applicants have applied for this job yet."));
        }

        //if users were applied to this job
        List<ApplicantsDTO> applicantResponse = jobApplication.stream().
                map(a -> new ApplicantsDTO(
                        a.getApplicant().getUsername(),
                        a.getApplicant().getEmail(),
                        a.getAppliedAt()
                )).toList();

        return ResponseEntity.ok(applicantResponse);

    }


    // get all applicants for the company
    public ResponseEntity<?> getAllApplicants(String email) {

        Users company=usersRepo.findByEmail(email).orElseThrow(()-> new UnAuthorizedException("Company",email));

        List<Job> companyJobs=jobRepo.findByCompany(company);

        if(companyJobs.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ApiResponse(LocalDateTime.now(),"Not Found","You haven't posted any jobs yet."));
        }

        List<JobApplication> jobApplications=jobApplicationRepo.findByJobIn(companyJobs);

        if(jobApplications.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ApiResponse(LocalDateTime.now(),"Not Found","No applicants found for your job postings.\""));
        }

        //if some user applied to a particular job in this company
        List<ApplicantsDTO> applicantResponse=jobApplications.stream().
                map(a-> new ApplicantsDTO(
                        a.getApplicant().getUserId(),
                        a.getJob().getJobId(),
                        a.getJob().getJobTitle(),
                        a.getApplicant().getUsername(),
                        a.getApplicant().getEmail(),
                        a.getAppliedAt()
                )).toList();
        return ResponseEntity.ok(applicantResponse);
    }


    // approve applicant
    public ResponseEntity approveApplicant(Integer applicationId, String email) {
        //validate applicant with appId
        JobApplication application = jobApplicationRepo.findById(applicationId).orElseThrow(() -> new NotFoundException("Application ID", applicationId));

        Users company = usersRepo.findByEmail(email).orElseThrow(() -> new UnAuthorizedException("Company Email ID", email));
        Job job=application.getJob();

        if (!job.getCompany().getEmail().equals(email)) {
            return ResponseEntity.ok
                    (new ApiResponse(LocalDateTime.now(), "Failure", "You are not allowed to approve applicants for this job."));
        }
        application.setStatus("APPROVED");
        jobApplicationRepo.save(application);
        subject="Congratulations! You are Selected";
        body="Dear " + application.getApplicant().getUsername() + ",\n\n" +
                "We are pleased to inform you that you have been SELECTED for the role "+application.getJob().getJobTitle()+"\n at " +application.getJob().getCompanyName()+".\n"
                +"Our HR team will contact you soon.\n\n" +
                "Regards,\n"+application.getJob().getCompany().getUsername()+" Team";

        emailService.sendEmail(application.getApplicant().getEmail(),subject,body);
        return ResponseEntity.ok
                (new ApiResponse(LocalDateTime.now(), "Success", "Approved successfully for Applicant ID:" + application.getApplicant().getUserId()));
    }


    //reject applicant
    public ResponseEntity rejectApplicant(Integer applicationId, String email) {
        JobApplication application = jobApplicationRepo.findById(applicationId).orElseThrow(() -> new NotFoundException("Application ID", applicationId));

        Users company = usersRepo.findByEmail(email).orElseThrow(() -> new UnAuthorizedException("Company Email ID", email));
        Job job=application.getJob();

        if (!job.getCompany().getEmail().equals(email)) {
            return ResponseEntity.ok
                    (new ApiResponse(LocalDateTime.now(), "Failure", "You are not allowed to approve applicants for this job."));
        }
        application.setStatus("REJECTED");
        jobApplicationRepo.save(application);
        subject="Application Status Update";
        body = "Dear " + application.getApplicant().getUsername() + ",\n\n" +
                "Thank you for your interest.\n" +
                "We regret to inform you that you are not selected for the role you applied for "+application.getJob().getJobTitle()+"\n\n" +
                "Best wishes for your future!\n"+application.getJob().getCompany().getUsername()+" Team";

        emailService.sendEmail(application.getApplicant().getEmail(),subject,body);

        return ResponseEntity.ok
                (new ApiResponse(LocalDateTime.now(), "Success", "Approved successfully for Applicant ID:" + application.getApplicant().getUserId()));
    }
}



