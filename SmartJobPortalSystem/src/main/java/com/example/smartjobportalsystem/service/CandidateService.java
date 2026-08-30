package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.dto.ApiResponse;
import com.example.smartjobportalsystem.dto.CandidateRegisterDTO;
import com.example.smartjobportalsystem.dto.LoginResponse;
import com.example.smartjobportalsystem.entity.Candidate;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.repository.CandidateRepository;
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
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JWTService jwtService;


    // registration service
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
        user.setRole("ROLE_USER");
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

    public ResponseEntity<?> updateCandidate(String email,CandidateRegisterDTO candidate) {
        List<String> updatedFields= new ArrayList<>();
        String newToken=null;

        Candidate cand = candidateRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Candidate not found with email "+email));
        Users user = usersRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found with email "+email));

        if(candidate.getFirstname()!=null && !candidate.getFirstname().trim().isEmpty()){
            cand.setFirstname(candidate.getFirstname());
            updatedFields.add("First name");
        }
        if(candidate.getLastname()!=null && !candidate.getLastname().trim().isEmpty()){
            cand.setLastname(candidate.getLastname());
            updatedFields.add("Last name");
        }
        if(candidate.getEmail()!=null && !candidate.getEmail().trim().isEmpty()){
            if(candidate.getEmail().equalsIgnoreCase(email)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same Email"));
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
            if(cand.getPassword().equals(candidate.getPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same password!"));
            }
            cand.setPassword(passwordEncoder.encode(candidate.getPassword()));
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
}
