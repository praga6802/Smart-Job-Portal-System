package com.example.smartjobportalsystem.service;


import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.exception.NameNotFoundException;

import com.example.smartjobportalsystem.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@Service
public class AuthService {

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;


    //login feature
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            Users user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new BadCredentialsException("Invalid Credentials!"));
            String jwtToken = jwtService.generateToken(loginRequest.getEmail());
            LoginResponse response = new LoginResponse("Success", "Login Successfully", jwtToken, user.getRole());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(LocalDateTime.now(),"Failure","Invalid Credentials"));
        }
    }



    //logout feature
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Logout Successfully!"));
    }





    public ResponseEntity<?> forgotPassword(String password,String reEnterPassword, String email) {
        Users user=usersRepo.findByEmail(email).orElseThrow(()->new NameNotFoundException("Email",email));
        if(password==null|| reEnterPassword==null || reEnterPassword.isBlank() || password.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","Password cannot be empty"));
        }
        if(passwordEncoder.matches(password, user.getPassword())
        || passwordEncoder.matches(reEnterPassword, user.getPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","Dont Enter the same Password"));
        }
        if(!password.equals(reEnterPassword)){
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponse(LocalDateTime.now(),"Failure","Password do not matches"));
        }
        String encodePassword=passwordEncoder.encode(password);
        user.setPassword(encodePassword);
        usersRepo.save(user);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(),"Success","Your password has been reset successfully..You are set to login"));
    }



}
