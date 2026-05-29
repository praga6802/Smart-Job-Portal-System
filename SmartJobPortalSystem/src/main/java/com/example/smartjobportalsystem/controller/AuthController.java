package com.example.smartjobportalsystem.controller;


import com.example.smartjobportalsystem.dto.*;
import com.example.smartjobportalsystem.entity.RefreshToken;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.service.AuthService;
import com.example.smartjobportalsystem.service.RefreshTokenService;
import com.example.smartjobportalsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5500")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtil jwtUtil;

    //register for admin, user and company
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO){
        return authService.registerUsers(registerDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    //update for admin, user and company
    @PutMapping({"/admin/updateUser","/user/updateUser","/company/updateUser"})
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO user){
        String existingEmail= SecurityContextHolder.getContext().getAuthentication().getName();
        return authService.updateUser(user,existingEmail);
    }

    @PutMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO password, @AuthenticationPrincipal UserDetails user){
        String email=user.getUsername();
        String p1=password.getPassword();
        String p2= password.getReEnterPassword();;
        return authService.forgotPassword(p1,p2,email);
    }


    @PostMapping("/refreshToken")
    public JWTResponseDTO getRefreshToken(@RequestBody RefreshTokenRequest request){
       return refreshTokenService.findByToken(request.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(user->{
                    String jwtToken=jwtUtil.generateToken(user);
                    return JWTResponseDTO.builder()
                            .timestamp(LocalDateTime.now())
                            .message("Success")
                            .token(jwtToken)
                            .refreshToken(request.getToken())
                            .build();
                }).orElseThrow(()->new NotFoundException("Refresh Token not found"));
    }

}
