package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Integer> {


    Optional<Verification> findByEmailAndOtpAndIsUsedFalse(String email, String otp);

    Optional<Verification> findByOtpAndIsUsedFalse(String otp);
}
