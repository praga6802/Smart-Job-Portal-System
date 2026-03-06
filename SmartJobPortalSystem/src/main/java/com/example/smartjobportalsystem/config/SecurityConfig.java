package com.example.smartjobportalsystem.config;


import com.example.smartjobportalsystem.filter.JWTAuthenticationFilter;
import com.example.smartjobportalsystem.filter.JWTValidationFilter;
import com.example.smartjobportalsystem.service.CustomUserDetailsService;
import com.example.smartjobportalsystem.util.JWTAuthenticationEntryPoint;
import com.example.smartjobportalsystem.util.JWTAuthenticationProvider;
import com.example.smartjobportalsystem.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

   private final CustomUserDetailsService userDetailsService;
   private final JwtUtil jwtUtil;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }


    //FILTER CHAINS
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager(), jwtUtil, objectMapper());
        JWTValidationFilter jwtValidationFilter = new JWTValidationFilter(authenticationManager());
        return http.authorizeHttpRequests(req->req.requestMatchers("/auth/register","/auth/login").permitAll()
                    .requestMatchers("/auth/user/**").hasRole("USER")
                    .requestMatchers("/auth/admin/**").hasRole("ADMIN")
                    .requestMatchers("/auth/company/**").hasRole("COMPANY")
                    .anyRequest().authenticated())
                .csrf(c->c.disable())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidationFilter, JWTAuthenticationFilter.class)
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider dao= new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(passwordEncoder());
        return dao;
    }

    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider(){
        return new JWTAuthenticationProvider(jwtUtil,userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return new ProviderManager(Arrays.asList(daoAuthenticationProvider(),jwtAuthenticationProvider()));
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }


}
