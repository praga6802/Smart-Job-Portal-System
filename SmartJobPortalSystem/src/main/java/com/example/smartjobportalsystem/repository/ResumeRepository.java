package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.entity.Candidate;
import com.example.smartjobportalsystem.entity.Resume;
import com.example.smartjobportalsystem.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {


    Optional<Resume> findByCandidate(Candidate candidate);
}
