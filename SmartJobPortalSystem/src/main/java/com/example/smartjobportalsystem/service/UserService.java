package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.dto.ApiResponse;
import com.example.smartjobportalsystem.dto.ApplicationStatusDTO;
import com.example.smartjobportalsystem.dto.JobDTO;
import com.example.smartjobportalsystem.entity.Job;
import com.example.smartjobportalsystem.entity.JobApplication;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.exception.NameNotFoundException;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.exception.UnAuthorizedException;
import com.example.smartjobportalsystem.repository.JobApplicationRepo;
import com.example.smartjobportalsystem.repository.JobRepo;
import com.example.smartjobportalsystem.repository.UsersRepo;
import io.jsonwebtoken.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    JobRepo jobRepo;

    @Autowired
    JobApplicationRepo jobApplicationRepo;


    //apply job for the company
    public ResponseEntity<?> applyJob(Integer jobId, String email) {
        Users applicant=usersRepo.findByEmail(email).orElseThrow(()-> new NameNotFoundException("Email",email));
        Job job=jobRepo.findById(jobId).orElseThrow(()-> new NotFoundException("Job ID",jobId));

        List<Job> rejectedJobs=jobRepo.findByStatus("REJECTED");
        boolean isRejected=rejectedJobs.stream().anyMatch(j->j.getJobId().equals(jobId));

        if(isRejected) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(LocalDateTime.now(),"Failure","No Job found for this Id"));

        if(jobApplicationRepo.existsByApplicantUserIdAndJobJobId(applicant.getUserId(),job.getJobId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","Already Applied with Email "+email));
        }

        JobApplication jobApplication= new JobApplication();
        jobApplication.setApplicant(applicant);
        jobApplication.setJob(job);
        jobApplication.setStatus("APPLIED");
        jobApplication.setAppliedAt(LocalDateTime.now());
        jobApplicationRepo.save(jobApplication);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Applied Successfully"));
    }

    //view Job Application Status
    public ResponseEntity<?> viewApplicationStatus(String email) {
        Users user=usersRepo.findByEmail(email).orElseThrow(()-> new UnAuthorizedException("Unauthorized User with Email ",email));

        List<JobApplication> applications=jobApplicationRepo.findByApplicant(user);

        if(applications.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","No Applications were found for User ID "+user.getUserId()));
        }

        List<ApplicationStatusDTO> response=applications.stream().
                map(a-> new ApplicationStatusDTO(
                        a.getJob().getJobTitle(),
                        a.getJob().getCompanyName(),
                        a.getJob().getSalary(),
                        a.getJob().getJobLocation(),
                        a.getStatus(),a.getAppliedAt()
                )).toList();
        return ResponseEntity.ok(response);
    }

    // find the job by company
    public ResponseEntity<?> findJobsByCompany(String c) {
        Users company = usersRepo.findByUsername(c).orElseThrow(() -> new NameNotFoundException("Company", c));

        List<Job> approvedJobs=jobRepo.findByCompanyAndStatus(company,"APPROVED");
        if(approvedJobs.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","No jobs were found for "+company.getUsername()));
        }

        List<JobDTO> response = approvedJobs.stream().
                    map(j -> new JobDTO(j.getJobId(),j.getJobTitle(), j.getSkills(), j.getJobDescription(), j.getSalary(), j.getExperience(), j.getJobLocation(), j.getJobType()))
                    .toList();
            return ResponseEntity.ok(response);
        }

        //upload resume
    public ResponseEntity<?> uploadResume(MultipartFile file, String email) throws IOException {
        //validating the user
        Users user=usersRepo.findByEmail(email).orElseThrow(()-> new NameNotFoundException("Email ID",email));

        //validating the user
        if(!"ROLE_USER".equalsIgnoreCase(user.getRole())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","Only Job Seekers can upload their resume."));
        }
        //check if the content is not empty
        if(file.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","File cannot be empty"));
        }

        //check if the file type mismatch
        String fileType = file.getContentType();
        if (!("application/pdf".equals(fileType) || "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(fileType))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(LocalDateTime.now(), "Failure", "Only PDF or DOCX files are allowed"));
        }

        //create folder for uploading resume
        String dirPath="uploads/";
        File dir=new File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String orgFileName=file.getOriginalFilename();
        String extension= Objects.requireNonNull(orgFileName).substring(orgFileName.indexOf("."));
        String fileName=user.getUsername()+"_"+ LocalDate.now()+extension;

        Path filePath= Paths.get(dirPath + fileName);

        //save file
        Files.write(filePath,file.getBytes());

        //save file path for the user role only
        user.setResumePath(filePath.toString());
        usersRepo.save(user);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Resume has been uploaded Successfully"));
    }

    //delete resume
    public ResponseEntity<?> deleteResume(String email) {
        Users user = usersRepo.findByEmail(email).orElseThrow(() -> new NameNotFoundException("Email ID", email));

        String resumePath=user.getResumePath();

        if(resumePath==null || resumePath.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(LocalDateTime.now(),"Failure","No resume found"));
        }

        Path path=Paths.get(resumePath);
        try{
            //delete file
            Files.deleteIfExists(path);

            //delete from db also
            user.setResumePath(null);
            usersRepo.save(user);
            return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Resume Deleted Successfully"));
        }
        catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","Error Deleting file"));
        }
    }

    //view resume
    public ResponseEntity<?> viewResume(String email) throws IOException {
        Users user = usersRepo.findByEmail(email).orElseThrow(() -> new NameNotFoundException("Email ID", email));

        String resumePath=user.getResumePath();

        if(resumePath==null || resumePath.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(LocalDateTime.now(),"Failure","No resume found"));
        }
        Path path=Paths.get(resumePath);
        byte[] fileBytes=Files.readAllBytes(path);

        HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.setContentDisposition(ContentDisposition.inline()
                .filename(path.getFileName().toString())
                .build());
        return new ResponseEntity<>(fileBytes,httpHeaders,HttpStatus.OK);
    }

    // download resume
    public ResponseEntity<?> downloadResume(String email) throws IOException {
        Users user = usersRepo.findByEmail(email).orElseThrow(() -> new NameNotFoundException("Email ID", email));

        String resumePath = user.getResumePath();

        if (resumePath == null || resumePath.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(LocalDateTime.now(), "Failure", "No resume found"));
        }
        Path path=Paths.get(resumePath);
        byte[] fileBytes=Files.readAllBytes(path);

        HttpHeaders header= new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.setContentDisposition(ContentDisposition.attachment().filename(path.getFileName().toString()).build());
        return new ResponseEntity<>(fileBytes,header,HttpStatus.OK);

    }
}

