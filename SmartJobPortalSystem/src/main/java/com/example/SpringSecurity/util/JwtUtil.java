package com.example.SpringSecurity.util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secret="my-secret-super-key-that-is-long-enough-1234567890!%";
    private final SecretKey key= Keys.hmacShaKeyFor(secret.getBytes());
    private final long expirationDate=1000*60*60;

    public String generateToken(String email){

        return Jwts.builder().
                setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expirationDate))
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    public boolean validateToken(UserDetails userDetails, String token){
        String email=extractEmail(token);
        System.out.println(userDetails.getUsername());
        return email.equals(userDetails.getUsername());
    }
}
