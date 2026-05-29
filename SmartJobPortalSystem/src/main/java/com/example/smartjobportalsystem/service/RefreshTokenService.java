package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.entity.RefreshToken;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.exception.NameNotFoundException;
import com.example.smartjobportalsystem.exception.NotFoundException;
import com.example.smartjobportalsystem.repository.RefreshTokenRepo;
import com.example.smartjobportalsystem.repository.UsersRepo;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private UsersRepo usersRepo;

    public RefreshToken createRefreshToken(String email){

        Users user= usersRepo.findByEmail(email).orElseThrow(()-> new NotFoundException(email));
        RefreshToken refreshToken=refreshTokenRepo.findByUserInfo(user).orElse(new RefreshToken());
        refreshToken.setUserInfo(user);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(600L));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepo.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepo.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepo.delete(token);
            throw new NameNotFoundException(token.getToken()+" Refresh Token has been expired, Please Log In again");
        }
        return token;
    }
}
