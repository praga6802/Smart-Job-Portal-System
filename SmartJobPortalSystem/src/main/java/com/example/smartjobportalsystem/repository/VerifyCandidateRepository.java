package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.entity.VerifyCandidate;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyCandidateRepository extends JpaRepository<VerifyCandidate, Integer> {
    Optional<VerifyCandidate> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByContact(String contact);
}
