package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.*;
import com.example.smartjobportalsystem.exception.NameNotFoundException;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.exception.UnAuthorizedException;
import com.example.smartjobportalsystem.repository.*;
import io.jsonwebtoken.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private JobApplicationRepo jobApplicationRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationRepo verificationRepo;

    //apply job for the company
    public ResponseEntity<?> applyJob(Integer jobId, String email) {
        Users applicant=usersRepo.findByEmail(email).orElseThrow(()-> new NameNotFoundException("Email",email));
        Job job=jobRepo.findById(jobId).orElseThrow(()-> new NotFoundException("Job ID",jobId));

        ResumeEntity resume = resumeRepository.findByUser(applicant).orElseThrow(()-> new RuntimeException("Resume is mandatory for applying this job"));

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
        jobApplication.setResume(resume);

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
    public List<JobDTO> viewJobsByCompany(String companyName) {
        Users company = usersRepo.findByUsername(companyName).orElseThrow(() -> new NameNotFoundException("Company", companyName));

        List<JobDTO> jobs= jobRepo.findAll().stream().filter(j->j.getStatus().equals("APPROVED"))
                .map(j-> new JobDTO(j.getJobId(),j.getJobTitle(),j.getJobDescription(),j.getSkills(),j.getSalary(),j.getExperience(),j.getJobLocation(),j.getJobType()))
                .toList();
        return jobs;
        }

        //upload resume
    public ResponseEntity<?> uploadResume(MultipartFile file, String email) throws IOException {
        //validating the user
        Users user=usersRepo.findByEmail(email).orElseThrow(()-> new NameNotFoundException("Email ID",email));

        //check if the file type mismatch
        String fileType = file.getContentType();
        if (!("application/pdf".equals(fileType) || "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(fileType))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(LocalDateTime.now(), "Failure", "Only PDF or DOCX files are allowed"));
        }

        //check if the content is not empty
        if(file.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","File cannot be empty"));
        }

        //validating the user
        if(!"ROLE_USER".equalsIgnoreCase(user.getRole())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","Only Job Seekers can upload their resume."));
        }


        //create folder for uploading resume
        String dirPath="uploads/resume/"+user.getUserId()+"/";
        File dir=new File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        //check if the user has already a resume
        Optional<ResumeEntity> existingResume = resumeRepository.findByUser(user);
        if(existingResume.isPresent()){
            String path=existingResume.get().getFilePath();
            if (path != null) {
                Path oldPath = Paths.get(existingResume.get().getFilePath());
                Files.deleteIfExists(oldPath);
            }
            resumeRepository.delete(existingResume.get());
        }


        //creating file name
        String reqFileName=file.getOriginalFilename();
        if(reqFileName == null || !reqFileName.contains(".")){
            return ResponseEntity.badRequest().body(new ApiResponse(LocalDateTime.now(),"Failure","Invalid file name"));
        }

        String extension= Objects.requireNonNull(reqFileName).substring(reqFileName.lastIndexOf("."));
        String fileName=user.getUsername()+"_"+ LocalDate.now()+extension;

        //creating file path
        Path filePath= Paths.get(dirPath + fileName);

        //writing file content into file path
        Files.write(filePath,file.getBytes());

        //save file path for the user role only
        ResumeEntity resume=new ResumeEntity();
        resume.setFileName(fileName);
        resume.setFilePath(filePath.toString());
        resume.setUser(user);

        resumeRepository.save(resume);

        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Resume uploaded Successfully"));
    }

//    //delete resume
    public ResponseEntity<?> deleteResume(String email) {
        Users user = usersRepo.findByEmail(email).orElseThrow(() -> new NameNotFoundException("Email ID", email));

        ResumeEntity resume = resumeRepository.findByUser(user).orElseThrow(()-> new RuntimeException("No Resume found for "+user.getUsername()));
        String resumePath=resume.getFilePath();

        if (resumePath == null || resumePath.isEmpty()) {
            resumeRepository.delete(resume);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(LocalDateTime.now(),"Failure","Resume record existed but file was missing. Record removed."));
        }

        Path path=Paths.get(resumePath);
        try{
            //delete file
            Files.deleteIfExists(path);
            resumeRepository.delete(resume);
            return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Resume Deleted Successfully"));
        }
        catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","Error Deleting file"));
        }
    }


//    //view resume
    public ResponseEntity<?> viewResume(String email) throws IOException {
        Users user = usersRepo.findByEmail(email).orElseThrow(() -> new NameNotFoundException("Email ID", email));

        ResumeEntity resume = resumeRepository.findByUser(user).orElseThrow(()-> new RuntimeException("Resume not found"));
        String resumePath=resume.getFilePath();

        if (resumePath == null || resumePath.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(LocalDateTime.now(),"Failure","No resume found for "+user.getUsername()));
        }

        Path path=Paths.get(resumePath);

        byte[] fileBytes=Files.readAllBytes(path);

        HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.setContentDisposition(ContentDisposition.inline().filename(path.getFileName().toString()).build());
        return new ResponseEntity<>(fileBytes,httpHeaders,HttpStatus.OK);
    }

    // download resume
    public ResponseEntity<?> downloadResume(String email) throws IOException {
        Users user = usersRepo.findByEmail(email).orElseThrow(() -> new NameNotFoundException("Email ID", email));

        ResumeEntity resume = resumeRepository.findByUser(user).orElseThrow(()-> new RuntimeException("Resume not found for "+user.getUsername()));
        String resumePath=resume.getFilePath();
        if (resumePath == null || resumePath.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(LocalDateTime.now(),"Failure","No resume found"));
        }

        Path path=Paths.get(resumePath);
        byte[] fileBytes=Files.readAllBytes(path);

        HttpHeaders header= new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.setContentDisposition(ContentDisposition.attachment().filename(path.getFileName().toString()).build());
        return new ResponseEntity<>(fileBytes,header,HttpStatus.OK);
    }

    //send the otp
    public ResponseEntity<?> verifyEmailAndSendCode(String logEmail, String email) {
        Users user = usersRepo.findByEmail(logEmail).orElseThrow(() -> new NameNotFoundException("Email ID", logEmail));

        if (!logEmail.equalsIgnoreCase(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponse(LocalDateTime.now(), "Failure", "Both login and given email not matches"));
        }
        int otp=100000+new Random().nextInt(900000);
        VerificationTable verificationTable=new VerificationTable(user, VerificationType.EMAIL,String.valueOf(otp),LocalDateTime.now().plusMinutes(10));
        verificationRepo.save(verificationTable);

        emailService.sendEmail(new EmailDTO(user.getEmail(), "Email Verification", "Your verification code is: " + otp+ "\n\n" +
                "This code will expire in 10 minutes.\n\n" +
                "Regards,\nSmart Job Portal Team"));

        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(), "Success", "Verification code has been sent Successfully"));
    }


    //verify the otp
    public ResponseEntity<?> verifyEmailCode(UserDetails users, String code) {
        Users user=usersRepo.findByEmail(users.getUsername()).orElseThrow(()-> new NameNotFoundException("Email ID",users.getUsername()));

        VerificationTable verification=verificationRepo.findByUserAndCodeAndTypeAndIsUsedFalse(user,code,VerificationType.EMAIL).
                orElseThrow(()-> new UnAuthorizedException("Invalid Code",code));

        if(verification.getIsUsed()){
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","Code has been already used"));
        }
        if(verification.getExpiresAt().isBefore(LocalDateTime.now())){
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","Code has been expired"));
        }
        verification.setIsUsed(true);
        verificationRepo.save(verification);
        usersRepo.save(user);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Email has been verified Successfully"));
    }


    // view all jobs posted
    public List<JobDTO> viewAllJobs() {
        List<JobDTO> response = jobRepo.findAll().stream().
                filter(j->j.getStatus().equals("APPROVED")).
                map(j -> new JobDTO(j.getCompanyName(),j.getJobId(),j.getJobTitle(), j.getJobDescription(), j.getSkills(), j.getSalary(), j.getExperience(),
                        j.getJobLocation(), j.getJobType()))
                .toList();
        return response;
    }
}


