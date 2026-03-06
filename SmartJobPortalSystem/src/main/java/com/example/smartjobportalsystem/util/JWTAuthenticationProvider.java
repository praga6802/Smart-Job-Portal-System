package com.example.smartjobportalsystem.util;

import com.example.smartjobportalsystem.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JWTAuthenticationProvider(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token =((JWTAuthenticationToken)authentication).getToken();

        if(token==null || token.isEmpty()){
            return null;
        }
        String username;
        try {
            username = jwtUtil.extractUserNameFromToken(token);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid JWT Token");
        }

        if (username == null) {
            throw new BadCredentialsException("Invalid JWT Token");
        }
        UserDetails userDetails=userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
