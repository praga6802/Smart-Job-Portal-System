package com.example.smartjobportalsystem.controller;


import com.example.smartjobportalsystem.dto.ForgotPasswordDTO;
import com.example.smartjobportalsystem.dto.LoginUserDTO;
import com.example.smartjobportalsystem.dto.RegisterDTO;
import com.example.smartjobportalsystem.dto.UpdateUserDTO;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    //register for admin, user and company
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO){
        return authService.registerUsers(registerDTO);
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

}
