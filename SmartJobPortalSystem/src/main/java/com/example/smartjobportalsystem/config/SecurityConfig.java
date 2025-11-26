package com.example.smartjobportalsystem.config;


import com.example.smartjobportalsystem.filter.UserJWTAuthenticationFilter;
import com.example.smartjobportalsystem.service.CustomUserDetailsService;
import com.example.smartjobportalsystem.util.JWTAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final UserJWTAuthenticationFilter userJwtFilter;
    private final JWTAuthenticationEntryPoint entryPoint;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          UserJWTAuthenticationFilter userJwtFilter,
                          JWTAuthenticationEntryPoint entryPoint) {

        this.userDetailsService = userDetailsService;
        this.userJwtFilter = userJwtFilter;
        this.entryPoint = entryPoint;
    }

    //FILTER CHAINS
    @Bean
    public SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        configureCommon(http);
        http
                .authorizeHttpRequests(req->req
                        .requestMatchers("/auth/register","/auth/login").permitAll()
                    .requestMatchers("/auth/user/**").hasRole("USER")
                    .requestMatchers("/auth/admin/**").hasRole("ADMIN")
                    .requestMatchers("/auth/company/**").hasRole("COMPANY")
                    .anyRequest().authenticated())
                .addFilterBefore(userJwtFilter,UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(userAuthenticationProvider());
        return http.build();
    }


    private void configureCommon(HttpSecurity http) throws Exception{
        http.csrf(c->c.disable())
                .exceptionHandling(e->e.authenticationEntryPoint(entryPoint))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // AUTHENTICATION PROVIDER
  @Bean
    public DaoAuthenticationProvider userAuthenticationProvider(){
        DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
  }


  // AUTHENTICATION MANAGER
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
      return config.getAuthenticationManager();
    }


}
