package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.dto.UserDTO;
import com.example.smartjobportalsystem.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {
    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

}
