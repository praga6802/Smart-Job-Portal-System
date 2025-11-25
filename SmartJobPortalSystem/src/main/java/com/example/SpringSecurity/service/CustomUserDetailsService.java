package com.example.SpringSecurity.service;

import com.example.SpringSecurity.entity.Users;
import com.example.SpringSecurity.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UsersRepo usersRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Users user=usersRepo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Email not found"));

       System.out.println("Loaded user: " + user.getEmail() + ", password: " + user.getPassword());
       return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
               .password(user.getPassword())
               .roles(user.getRole().replace("ROLE_",""))
               .build();
    }
}
