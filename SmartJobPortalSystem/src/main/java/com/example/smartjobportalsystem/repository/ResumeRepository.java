package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.entity.ResumeEntity;
import com.example.smartjobportalsystem.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<ResumeEntity, Integer> {

    Optional<ResumeEntity> findByUser(Users user);
}
