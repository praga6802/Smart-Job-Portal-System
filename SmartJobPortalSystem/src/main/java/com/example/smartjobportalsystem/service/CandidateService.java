package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.*;
import com.example.smartjobportalsystem.enums.JobStatus;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private VerificationRepository verificationRepository;

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

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerifyCandidateRepository verifyCandidateRepository;

    @Autowired
    private EmailUpdateRepository emailUpdateRepository;


    // candidate registration
    public ResponseEntity<?> register(CandidateRegisterDTO candidate) {

        if (candidateRepository.existsByEmail(candidate.getEmail()) || usersRepository.existsByEmail(candidate.getEmail()) || verifyCandidateRepository.existsByEmail(candidate.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseDTO(LocalDateTime.now(), "Failure", "Email already exists!"));
        }

        if (candidateRepository.existsByContact(candidate.getContact()) || verifyCandidateRepository.existsByContact(candidate.getContact())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseDTO(LocalDateTime.now(), "Failure", "Mobile Number already exists!"));
        }

        //storing candidate temporarily
        VerifyCandidate verifyCandidate = new VerifyCandidate();
        if (candidate.getFirstname() != null && !candidate.getFirstname().isBlank()) {
            verifyCandidate.setFirstname(candidate.getFirstname().trim());
        }

        if (candidate.getLastname() != null && !candidate.getLastname().isBlank()) {
            verifyCandidate.setLastname(candidate.getLastname().trim());
        }

        if (candidate.getEmail() != null && !candidate.getEmail().isBlank()) {
            verifyCandidate.setEmail(candidate.getEmail().trim().toLowerCase());
        }

        if (candidate.getPassword() != null && !candidate.getPassword().isBlank()) {
            verifyCandidate.setPassword(passwordEncoder.encode(candidate.getPassword().trim()));
        }

        if (candidate.getContact() != null && !candidate.getContact().isBlank()) {
            verifyCandidate.setContact(candidate.getContact().trim());
        }

        if (candidate.getDob() != null && !candidate.getDob().isBlank()) {
            verifyCandidate.setDateOfBirth(candidate.getDob().trim());
        }

        if (candidate.getExperience() != null && !candidate.getExperience().isBlank()) {
            verifyCandidate.setExperience(candidate.getExperience().trim());
        }

        if (candidate.getSkills() != null && !candidate.getSkills().isBlank()) {
            verifyCandidate.setSkills(candidate.getSkills().trim());
        }

        verifyCandidate.setCreatedAt(LocalDateTime.now());
        verifyCandidateRepository.save(verifyCandidate);

        return verifyEmail(candidate.getEmail());
    }

    // Update Candidate Details
    @Transactional
    public ResponseEntity<?> updateCandidate(Integer candidateId, CandidateRegisterDTO candidate) {
        List<String> updatedFields = new ArrayList<>();
        String newToken = null;

        Users user = usersRepository.findById(candidateId).orElseThrow(() -> new NotFoundException("User not found!"));
        Candidate cand = candidateRepository.findByUser_UserId(candidateId).orElseThrow(() -> new NotFoundException("Candidate not found!"));


        if (candidate.getFirstname() != null && !candidate.getFirstname().trim().isEmpty()) {
            cand.setFirstname(candidate.getFirstname().trim());
            updatedFields.add("First name");
        }

        if (candidate.getLastname() != null && !candidate.getLastname().trim().isEmpty()) {
            cand.setLastname(candidate.getLastname().trim());
            updatedFields.add("Last name");
        }

        if (candidate.getEmail() != null && !candidate.getEmail().trim().isEmpty()) {
            if (cand.getEmail().equals(candidate.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponseDTO(LocalDateTime.now(), "Failure", "Do not enter same email"));
            }
            if (candidateRepository.existsByEmail(candidate.getEmail()) || usersRepository.existsByEmail(candidate.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponseDTO(LocalDateTime.now(), "Failure", "Email already taken!"));
            }
            VerifyCandidate verifyCandidate = new VerifyCandidate();
            verifyCandidate.setEmail(candidate.getEmail());
            verifyCandidateRepository.save(verifyCandidate);

            return verifyEmail(candidate.getEmail());
        }

        if (candidate.getContact() != null && !candidate.getContact().trim().isEmpty()) {
            if (cand.getContact().equals(candidate.getContact())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponseDTO(LocalDateTime.now(), "Failure", "Do not enter same contact!"));
            }

            if (candidateRepository.existsByContact(candidate.getContact())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponseDTO(LocalDateTime.now(), "Failure", "Contact already registered!"));
            }
            cand.setContact(candidate.getContact().trim());
            updatedFields.add("Contact");
        }

        if (candidate.getDob() != null && !candidate.getDob().trim().isEmpty()) {
            cand.setDateOfBirth(candidate.getDob().trim());
            updatedFields.add("Date of Birth");
        }

        if (candidate.getExperience() != null && !candidate.getExperience().trim().isEmpty()) {
            cand.setExperience(candidate.getExperience().trim());
            updatedFields.add("Experience");
        }

        if (candidate.getSkills() != null && !candidate.getSkills().trim().isEmpty()) {
            cand.setSkills(candidate.getSkills().trim());
            updatedFields.add("Skills");
        }
        candidateRepository.save(cand);


        String message = "Company " + String.join(",", updatedFields) + " Updated Successfully!";
        if (newToken != null) {
            return ResponseEntity.ok(new LoginResponseDTO(LocalDateTime.now(), "Success", message, newToken));
        }
        return ResponseEntity.ok(new LoginResponseDTO(LocalDateTime.now(), "Success", message));
    }


    // apply job
    public ResponseEntity<?> applyJob(Integer jobId, Integer candidateId) {

        Candidate candidate = candidateRepository.findByUser_UserId(candidateId).orElseThrow(() -> new NotFoundException("Candidate not found with id: " + candidateId));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found with id: " + jobId));


        if (job.getStatus() != JobStatus.APPROVED) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponseDTO(LocalDateTime.now(), "Failure", "Job is not approved by admin!"));
        }
        if (!job.isActive()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponseDTO(LocalDateTime.now(), "Failure", "No longer accepting applications!"));
        }

        if (jobApplicationRepository.existsByCandidate_CandidateIdAndJob_JobId(candidate.getCandidateId(), job.getJobId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponseDTO(LocalDateTime.now(), "Failure", "You have already applied for this job!"));
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setCandidate(candidate);
        jobApplication.setJob(job);
        jobApplicationRepository.save(jobApplication);


        return ResponseEntity.ok(new LoginResponseDTO(LocalDateTime.now(), "Success", "Job Applied Successfully"));
    }


    // get job by company
    public ResponseEntity<?> getJobsByCompany(String companyName) {
        Company company = companyRepository.findByName(companyName).orElseThrow(() -> new NotFoundException("Company not found"));

        List<Job> companyJobList = jobRepository.findByCompanyAndStatusAndActive(company, JobStatus.APPROVED, true);
        if (companyJobList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponseDTO(LocalDateTime.now(), "Failure", "No jobs were found!"));
        }

        List<CompanyJobResponseDTO> jobList = companyJobList.stream().map(CompanyJobResponseDTO::new).toList();
        return ResponseEntity.ok(jobList);
    }

    // get all jobs
    public ResponseEntity<?> getJobs() {
        List<Job> jobs = jobRepository.findByStatusAndActive(JobStatus.APPROVED, true);
        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponseDTO(LocalDateTime.now(), "Failure", "No jobs were found!"));
        }
        List<JobResponseDTO> jobList = jobs.stream().map(JobResponseDTO::new).toList();

        return ResponseEntity.ok(jobList);
    }

    // view application status
    public ResponseEntity<?> viewApplicationStatus(Integer candidateId) {
        Candidate candidate = candidateRepository.findByUser_UserId(candidateId).orElseThrow(() -> new NotFoundException("Candidate not found with id: " + candidateId));

        List<JobApplication> jobApplications = jobApplicationRepository.findByCandidate(candidate);

        if (jobApplications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponseDTO(LocalDateTime.now(), "Failure", "No Job applications were found!"));
        }

        List<ApplicationStatusDTO> statusList = jobApplications.stream().map(ApplicationStatusDTO::new).toList();
        return ResponseEntity.ok(statusList);
    }

    // RESUME FEATURES
    // upload resume
    public ResponseEntity<?> uploadResume(MultipartFile file, Integer candidateId) throws IOException {
        Candidate candidate = candidateRepository.findByUser_UserId(candidateId).orElseThrow(() -> new NotFoundException("Candidate not found with ID: " + candidateId));

        String fileType = file.getContentType();
        if (!("application/pdf".equals(fileType) || "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(fileType))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO(LocalDateTime.now(), "Failure", "Only PDF or DOCX files are allowed"));
        }

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiResponseDTO(LocalDateTime.now(), "Failure", "File cannot be empty"));
        }

      //create folder for uploading resume
        String dirPath = "uploads/resume/" + candidateId + "/";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
//
//        //check if the user has already a resume
        Optional<Resume> existingResume = resumeRepository.findByCandidate(candidate);
        if(existingResume.isPresent()){
            String path=existingResume.get().getFilePath();
            if (path != null) {
                Path oldPath = Paths.get(existingResume.get().getFilePath());
                Files.deleteIfExists(oldPath);
            }
            resumeRepository.delete(existingResume.get());
        }
//
//
//        //creating file name
        String reqFileName=file.getOriginalFilename();
        if(reqFileName == null || !reqFileName.contains(".")){
            return ResponseEntity.badRequest().body(new ApiResponseDTO(LocalDateTime.now(),"Failure","Invalid file name"));
        }
//
        String extension= Objects.requireNonNull(reqFileName).substring(reqFileName.lastIndexOf("."));
        String fileName=candidate.getFirstname()+"_"+candidate.getLastname()+"_"+LocalDate.now()+extension;

        //creating file path
        Path filePath= Paths.get(dirPath + fileName);

        //writing file content into file path
        Files.write(filePath,file.getBytes());

        //save file path for the user role only
        Resume resume=new Resume();
        resume.setFileName(fileName);
        resume.setFilePath(filePath.toString());
        resume.setCandidate(candidate);

        resumeRepository.save(resume);

        return ResponseEntity.ok(new ApiResponseDTO(LocalDateTime.now(),"Success","Resume uploaded Successfully"));
    }


    //delete resume
    public ResponseEntity<?> deleteResume(Integer candidateId) throws IOException{
        Candidate candidate = candidateRepository.findByUser_UserId(candidateId).orElseThrow(() -> new NotFoundException("Candidate not found with this ID: ", candidateId));
        Resume resume = resumeRepository.findByCandidate(candidate).orElseThrow(()-> new NotFoundException("No Resume found for "+candidate.getFirstname()));
        String resumePath=resume.getFilePath();

        if (resumePath == null || resumePath.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO(LocalDateTime.now(),"Failure","Resume File is missing!"));
        }

        Path path=Paths.get(resumePath);
        Files.deleteIfExists(path);
        resumeRepository.delete(resume);
        return ResponseEntity.ok(new ApiResponseDTO(LocalDateTime.now(),"Success","Resume Deleted Successfully"));
    }



    //view resume
    public ResponseEntity<?> viewResume(Integer candidateId) throws IOException {
        Candidate candidate = candidateRepository.findByUser_UserId(candidateId).orElseThrow(() -> new NotFoundException("Candidate not found with this ID: ", candidateId));

        Resume resume = resumeRepository.findByCandidate(candidate).orElseThrow(()-> new NotFoundException("No Resume found for "+candidate.getFirstname()));
        String resumePath=resume.getFilePath();

        if (resumePath == null || resumePath.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO(LocalDateTime.now(),"Failure","Resume File is missing!"));
        }

        Path path=Paths.get(resumePath);
        byte[] fileBytes=Files.readAllBytes(path);

        HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.setContentDisposition(ContentDisposition.inline().filename(path.getFileName().toString()).build());
        return new ResponseEntity<>(fileBytes,httpHeaders,HttpStatus.OK);
    }

    // download resume
    public ResponseEntity<?> downloadResume(Integer candidateId) throws IOException {
        Candidate candidate = candidateRepository.findByUser_UserId(candidateId).orElseThrow(() -> new NotFoundException("Candidate not found for this ID: "+candidateId));

        Resume resume = resumeRepository.findByCandidate(candidate).orElseThrow(()-> new NotFoundException("No Resume found for "+candidate.getFirstname()));
        String resumePath=resume.getFilePath();

        if (resumePath == null || resumePath.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO(LocalDateTime.now(),"Failure","Resume file is missing"));
        }

        Path path=Paths.get(resumePath);
        byte[] fileBytes=Files.readAllBytes(path);

        HttpHeaders header= new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.setContentDisposition(ContentDisposition.attachment().filename(path.getFileName().toString()).build());
        return new ResponseEntity<>(fileBytes,header,HttpStatus.OK);
    }

    // verify candidate email and sent OTP
    public ResponseEntity<?> verifyEmail(String email) {

        String candidateName = verifyCandidateRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Candidate not found")).getFirstname();
        int otp=100000+new Random().nextInt(900000);

        Verification verification=new Verification();
        verification.setEmail(email);
        verification.setOtp(String.valueOf(otp));
        verification.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        verification.setVerified(false);
        verification.setUsed(false);

        verificationRepository.save(verification);

        emailService.sendEmail(
                new EmailRequestDTO(email, "Email Verification - Smart Job Portal",
                        "Hello, " + candidateName + "\n\n" +
                        "Thank you for registering with Smart Job Portal.\n\n" +
                        "Your One-Time Password (OTP) for email verification is:\n\n" +
                        "OTP: " + otp + "\n\n" +
                        "This OTP is valid for 10 minutes. Please do not share this OTP with anyone.\n\n" +
                        "If you did not initiate this registration, you can safely ignore this email.\n\n" +
                        "Regards,\n" +
                        "Smart Job Portal Team"
                )
        );

        return ResponseEntity.ok(new ApiResponseDTO(LocalDateTime.now(), "Success", "OTP has been sent to your Email: "+email));
    }

    // verify OTP for register user
    public ResponseEntity<?> verifyOtp(String email, String otp) {

        VerifyCandidate verifyCandidate = verifyCandidateRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Email not matches!"));

        Verification verification = verificationRepository.findByEmailAndOtpAndIsUsedFalse(email,otp)
                .orElseThrow(()-> new NotFoundException("Invalid OTP: "+otp));

        if(verification.getExpiresAt().isBefore(LocalDateTime.now())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponseDTO(LocalDateTime.now(),"Failure","OTP has expired!"));
        }


        // create user details
        Users user = new Users();
        user.setEmail(verifyCandidate.getEmail());
        user.setPassword(verifyCandidate.getPassword());
        user.setRole("CANDIDATE");
        usersRepository.save(user);

        // create candidate details
        Candidate newCandidate = new Candidate();
        newCandidate.setFirstname(verifyCandidate.getFirstname());
        newCandidate.setLastname(verifyCandidate.getLastname());
        newCandidate.setEmail(verifyCandidate.getEmail());
        newCandidate.setContact(verifyCandidate.getContact());
        newCandidate.setDateOfBirth(verifyCandidate.getDateOfBirth());
        newCandidate.setExperience(verifyCandidate.getExperience());
        newCandidate.setSkills(verifyCandidate.getSkills());
        newCandidate.setUser(user);
        newCandidate.setVerified(true);

        candidateRepository.save(newCandidate);

        verification.setUsed(true); // otp is used
        verification.setVerified(true); // email is verified
        verificationRepository.save(verification);

        verifyCandidateRepository.delete(verifyCandidate); // delete temporary candidate details
        verificationRepository.delete(verification);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO(LocalDateTime.now(),"Success", "Candidate registered successfully!"));
    }

    // verify otp for email update
    public ResponseEntity<?> verifyEmailUpdate(String otp, Integer userId) {
        Verification verification = verificationRepository.findByOtpAndIsUsedFalse(otp)
                .orElseThrow(()-> new NotFoundException("Invalid OTP: "+otp));

        if(verification.getExpiresAt().isBefore(LocalDateTime.now())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponseDTO(LocalDateTime.now(),"Failure","OTP has expired!"));
        }

        VerifyCandidate verifyCandidate = verifyCandidateRepository.findByEmail(verification.getEmail()).orElseThrow(()-> new NotFoundException("New email not found"));


        Users users = usersRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found!"));
        Candidate candidate = candidateRepository.findByUser_UserId(userId).orElseThrow(()-> new NotFoundException("Candidate not found!"));

        users.setEmail(verifyCandidate.getEmail());
        usersRepository.save(users);

        candidate.setEmail(verifyCandidate.getEmail());
        candidateRepository.save(candidate);

        verification.setVerified(true);
        verification.setUsed(true);
        verificationRepository.save(verification);

        verifyCandidateRepository.delete(verifyCandidate);
        verificationRepository.delete(verification);

        String token = jwtService.generateToken(verifyCandidate.getEmail());
        return ResponseEntity.ok(new LoginResponseDTO(LocalDateTime.now(),"Success","Email Updated Successfully",token));


    }
}
