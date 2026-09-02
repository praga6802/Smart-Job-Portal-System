package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.dto.ApiResponse;
import com.example.smartjobportalsystem.dto.CompanyRegisterDTO;
import com.example.smartjobportalsystem.dto.JobDTO;
import com.example.smartjobportalsystem.dto.LoginResponse;
import com.example.smartjobportalsystem.entity.Company;
import com.example.smartjobportalsystem.entity.Job;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.exception.UnAuthorizedException;
import com.example.smartjobportalsystem.repository.CompanyRepository;
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
        user.setRole("COMPANY");
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

    // update company details
    @Transactional
    public ResponseEntity<?> updateCompany(Integer userId,CompanyRegisterDTO company) {
        List<String> updatedFields= new ArrayList<>();
        String newToken =null;

        Users user = usersRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found!"));
        Company comp = companyRepository.findByUserUserId(userId).orElseThrow(()-> new NotFoundException("Company not found!"));


        if(company.getName()!=null && !company.getName().trim().isEmpty()){
            comp.setName(company.getName());
            updatedFields.add("Name");
        }

        if(company.getEmail()!=null && !company.getEmail().trim().isEmpty()){
            if(company.getEmail().equals(comp.getEmail())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same Email"));
            }

            if(companyRepository.existsByEmail(company.getEmail())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponse(LocalDateTime.now(),"Failure","Email already taken!"));
            }
            comp.setEmail(company.getEmail());
            user.setEmail(company.getEmail());
            usersRepository.save(user);
            updatedFields.add("Email");
            newToken=jwtService.generateToken(company.getEmail());
        }

        if(company.getContact()!=null && !company.getContact().trim().isEmpty()){
            if(comp.getContact().equals(company.getContact())){
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
            if(comp.getUrl().equals(company.getUrl())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","Do not enter same URL!"));
            }

            if(companyRepository.existsByUrl(company.getUrl())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(LocalDateTime.now(),"Failure","URL already registered!"));
            }
            comp.setUrl(company.getUrl());
            updatedFields.add("URL");
        }

        if(company.getGst()!=null && !company.getGst().trim().isEmpty()){
            if(comp.getGst().equals(company.getGst())){
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

    // Post New Job
    public ResponseEntity<?> postJob(JobDTO job, Integer companyId){
        Company company=companyRepository.findByUserUserId(companyId).orElseThrow(()-> new NotFoundException("Company not found!"));
        Job j= new Job();
        j.setTitle(job.getTitle());
        j.setDescription(job.getDescription());
        j.setSalary(job.getSalary());
        j.setExperience(job.getExperience());
        j.setLocation(job.getLocation());
        j.setType(job.getType());
        j.setSkills(job.getSkills());

        j.setCompany(company);

        jobRepository.save(j);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","New Job has been posted Successfully"));
    }

}

