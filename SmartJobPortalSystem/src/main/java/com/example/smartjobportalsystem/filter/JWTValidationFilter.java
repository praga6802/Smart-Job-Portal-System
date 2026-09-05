package com.example.smartjobportalsystem.filter;


import com.example.smartjobportalsystem.dto.LoginResponseDTO;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.pojo.MyUserDetails;
import com.example.smartjobportalsystem.service.JWTService;
import com.example.smartjobportalsystem.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JWTValidationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        String token="";
        String username="";

        String path=request.getServletPath();
        if(path.equals("/auth/register") || path.equals("/auth/login")){
            filterChain.doFilter(request,response);
            return;
        }

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token =authHeader.substring(7);
            username=jwtService.extractUsername(token);

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

                if(jwtService.validateToken(token,userDetails)){
                    Integer tokenVersion = jwtService.extractTokenVersion(token);

                    MyUserDetails myUserDetails = (MyUserDetails) userDetails;
                    Integer currentTokenVersion = myUserDetails.getUser().getTokenVersion();

                    if (!tokenVersion.equals(currentTokenVersion)) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        response.setContentType("application/json");
                        response.getWriter().write(
                                "{\"status\":\"Failure\",\"message\":\"Session Expired! Please Log In again!\"}"
                        );
                        return;
                    }

                    UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    System.out.println("Username: " + userDetails.getUsername());
                    System.out.println("Authorities: " + userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
