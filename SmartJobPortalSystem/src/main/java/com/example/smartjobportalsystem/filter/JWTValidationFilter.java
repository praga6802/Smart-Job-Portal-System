package com.example.smartjobportalsystem.filter;

import com.example.smartjobportalsystem.util.JWTAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTValidationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path=request.getServletPath();
        if(path.equals("/auth/signup") || (path.equals("/auth/login"))){
            filterChain.doFilter(request,response);
            return;
        }

        String token=extractJWTToken(request);
        if(token!=null) {
            try {
                JWTAuthenticationToken jwtAuthenticationToken = new JWTAuthenticationToken(token);
                Authentication authResult = authenticationManager.authenticate(jwtAuthenticationToken);

                if (authResult.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Invalid JWT token\"}");
                return;
            }
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing JWT token\"}");
            return;
        }
        filterChain.doFilter(request,response);
    }


    public String extractJWTToken(HttpServletRequest request){
        String token=request.getHeader("Authorization");
        if(token!=null && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }
}
