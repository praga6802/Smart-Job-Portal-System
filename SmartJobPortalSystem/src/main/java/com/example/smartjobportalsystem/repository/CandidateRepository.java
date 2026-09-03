package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate,Integer> {
    boolean existsByEmail(String email);

    boolean existsByContact(String contact);

    Optional<Candidate> findByEmail(String email);

    Optional<Candidate> findByUser_UserId(Integer candidateId);

    @Query("SELECT COUNT(c) FROM Candidate c")
    Long getTotalCandidates();
}
