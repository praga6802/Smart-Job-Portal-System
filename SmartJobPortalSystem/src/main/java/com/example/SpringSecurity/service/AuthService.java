package com.example.SpringSecurity.service;


import com.example.SpringSecurity.dto.ApiResponse;
import com.example.SpringSecurity.dto.LoginResponse;
import com.example.SpringSecurity.dto.LoginUserDTO;
import com.example.SpringSecurity.dto.UpdateUserDTO;
import com.example.SpringSecurity.entity.Users;
import com.example.SpringSecurity.exception.AlreadyExistsException;
import com.example.SpringSecurity.exception.NameNotFoundException;
import com.example.SpringSecurity.exception.NotFoundException;
import com.example.SpringSecurity.repository.UsersRepo;
import com.example.SpringSecurity.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class AuthService {

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    //login user or admin or company
    public ResponseEntity<LoginResponse> login(LoginUserDTO user) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                    body(new LoginResponse(new ApiResponse(LocalDateTime.now(),"Failure","Invalid Credentials"),null));
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).
                body(new LoginResponse(new ApiResponse(LocalDateTime.now(),"Success","Login Successfully"),token));
    }


    //registers user or admin or company
    public ResponseEntity<ApiResponse> registerUsers(Users users) {
        if (usersRepo.existsByEmail(users.getEmail())) {
            throw new AlreadyExistsException("Email", users.getEmail());
        }

        users.setUsername(users.getUsername()); // set proper display name
        users.setEmail(users.getEmail().toLowerCase());
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setRole("ROLE_"+ users.getRole().toUpperCase());

        usersRepo.save(users);
        return ResponseEntity.ok(new ApiResponse(LocalDateTime.now(), "Success", "Sign Up Successfully"));
    }

    //update user or admin or company
    public ResponseEntity<?> updateUser(UpdateUserDTO user, String existingEmail) {

        Users existingUser= usersRepo.findByEmail(existingEmail).orElseThrow(()-> new UsernameNotFoundException("Email not found"));

        //block password
        if(user.getPassword() != null && !user.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(LocalDateTime.now(), "Failed", "Password cannot be changed here"));
        }

        //update email
        boolean emailUpdated=false;
        // check the unique email in database
        if (user.getEmail()!=null && !user.getEmail().isBlank()) {
            //new email is present in db or not
            boolean existsByEmail= usersRepo.existsByEmail(user.getEmail());
            if(existsByEmail){
                return ResponseEntity.status(HttpStatus.FOUND).
                        body(new ApiResponse(LocalDateTime.now(),"Already Exists","Email "+user.getEmail()+" is already taken"));
            }
            existingUser.setEmail(user.getEmail());
            emailUpdated=true;
        }

        boolean userNameUpdated=checkAndSet(user.getUsername(), existingUser.getUsername(), existingUser::setUsername, "Username");
        boolean roleUpdated=checkAndSet(user.getRole(), existingUser.getRole(), existingUser::setRole, "Role");

        usersRepo.save(existingUser);

        Map<String,Object> updateResult= new HashMap<>();

        if(user.getEmail()!=null){
            updateResult.put("Email:",emailUpdated?existingUser.getEmail():"Unchanged");
        }
        if (user.getUsername() != null) {
            updateResult.put("Username:", userNameUpdated ? existingUser.getUsername() : "Unchanged");
        }
        if (user.getRole() != null) {
            updateResult.put("Role:", roleUpdated ? existingUser.getRole() : "Unchanged");
        }

        if (emailUpdated) {
            String newToken = jwtUtil.generateToken(existingUser.getEmail());
            return ResponseEntity.ok(Map.of(
                    "LocalTime",LocalDateTime.now(),
                    "Status","Success",
                    "message", "New Email Updated",
                    "newToken", newToken
            ));
        }

        return ResponseEntity.ok(Map.of(
                "TimeStamp",LocalDateTime.now(),
                "Status","Success",
                "Message","User Details Updated Successfully",
                "Updated Fields",updateResult
        ));
    }

    private boolean checkAndSet(String newValue, String existingValue, Consumer<String> setter, String fieldName){
        if(newValue!=null && !newValue.isBlank()){
            if(newValue.equals(existingValue)){
                return false;
            }
            setter.accept(newValue);
            return true;
        }
        return false;
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
