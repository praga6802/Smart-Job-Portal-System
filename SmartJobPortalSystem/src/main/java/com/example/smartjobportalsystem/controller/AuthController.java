package com.example.smartjobportalsystem.controller;



import com.example.smartjobportalsystem.dto.LoginRequest;
import com.example.smartjobportalsystem.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5500", allowCredentials = "true")
public class AuthController {

    @Autowired
    AuthService authService;

//    @Autowired
//    private RefreshTokenService refreshTokenService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        return authService.logout();
    }

//    @PutMapping({"/admin/updateUser","/user/updateUser","/company/updateUser"})
//    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO user){
//        String existingEmail= SecurityContextHolder.getContext().getAuthentication().getName();
//        return authService.updateUser(user,existingEmail);
//    }

//    @PutMapping("/forgotPassword")
//    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO password, @AuthenticationPrincipal UserDetails user){
//        String email=user.getUsername();
//        String p1=password.getPassword();
//        String p2= password.getReEnterPassword();;
//        return authService.forgotPassword(p1,p2,email);
//    }


//    @PostMapping("/refreshToken")
//    public JWTResponseDTO getRefreshToken(@RequestBody RefreshTokenRequest request){
//       return refreshTokenService.findByToken(request.getToken())
//                .map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getUserInfo)
//                .map(user->{
//                    String jwtToken=jwtUtil.generateToken(user);
//                    return JWTResponseDTO.builder()
//                            .timestamp(LocalDateTime.now())
//                            .message("Success")
//                            .token(jwtToken)
//                            .refreshToken(request.getToken())
//                            .build();
//                }).orElseThrow(()->new NotFoundException("Refresh Token not found"));
//    }

}
