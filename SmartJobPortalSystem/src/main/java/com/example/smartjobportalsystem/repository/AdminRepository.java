package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    boolean existsByEmail(String email);

    boolean existsByContact(String contact);

    Optional<Admin> findByEmail(String email);

    Optional<Admin> findByUserUserId(Integer adminId);
}
