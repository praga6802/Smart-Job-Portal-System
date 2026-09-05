package com.example.smartjobportalsystem.service;


import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.exception.NameNotFoundException;

import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.pojo.MyUserDetails;
import com.example.smartjobportalsystem.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;


    //login feature
    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            MyUserDetails user = (MyUserDetails) authentication.getPrincipal();

            String jwtToken = jwtService.generateToken(user.getEmail());
            LoginResponseDTO response = new LoginResponseDTO(LocalDateTime.now(),"Success", "Login Successfully", user.getRole(), jwtToken);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(LocalDateTime.now(),"Failure","Invalid Credentials"));
        }
    }


    //logout feature
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(new ApiResponseDTO(LocalDateTime.now(),"Success","Logout Successfully!"));
    }


    // forgot password and reset
    public ResponseEntity<?> forgotPassword(String password,String confirmPassword, Integer userId) {
        Users user=usersRepository.findById(userId).orElseThrow(()->new NotFoundException("User not found with this ID: "+userId));

        if(password==null|| password.isBlank() || confirmPassword==null || confirmPassword.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiResponseDTO(LocalDateTime.now(),"Failure","Password cannot be empty"));
        }

        if(!password.equals(confirmPassword)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDTO(LocalDateTime.now(),"Failure","New Password and Confirm password do not match"));
        }

        if(passwordEncoder.matches(password, user.getPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponseDTO(LocalDateTime.now(),"Failure","Do not enter the same password as your current password!"));
        }

        String encodedPassword=passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setTokenVersion(user.getTokenVersion()+1);
        usersRepository.save(user);
        return ResponseEntity.ok(new ApiResponseDTO(LocalDateTime.now(),"Success","Your password has been reset successfully! Please login again."));
    }


}
