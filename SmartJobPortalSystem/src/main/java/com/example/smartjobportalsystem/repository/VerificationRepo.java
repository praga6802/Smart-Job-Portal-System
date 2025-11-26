package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.dto.VerificationType;
import com.example.smartjobportalsystem.entity.Users;
import com.example.smartjobportalsystem.entity.VerificationTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepo extends JpaRepository<VerificationTable, Integer> {

    Optional<VerificationTable>findByUserAndCodeAndTypeAndIsUsedFalse(Users users, String code, VerificationType type);
}
