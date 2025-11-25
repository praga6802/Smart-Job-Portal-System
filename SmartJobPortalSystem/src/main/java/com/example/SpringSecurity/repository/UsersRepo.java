package com.example.SpringSecurity.repository;

import com.example.SpringSecurity.dto.UserDTO;
import com.example.SpringSecurity.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsersRepo extends JpaRepository<Users,Integer> {
    Optional<Users> findByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Users> findByEmail(String email);

    @Query("""
    SELECT new com.example.SpringSecurity.dto.UserDTO(
        u.id, u.username, u.email, u.role
    )
    FROM Users u
    WHERE u.role = 'ROLE_ADMIN'
""")
    List<UserDTO> getAllAdmins();


    @Query("""
    SELECT new com.example.SpringSecurity.dto.UserDTO(
        u.id, u.username, u.email, u.role
    )
    FROM Users u
    WHERE u.role = 'ROLE_USER'
""")
    List<UserDTO> getAllUsers();

    @Query("""
    SELECT new com.example.SpringSecurity.dto.UserDTO(
        u.id, u.username, u.email, u.role
    )
    FROM Users u
    WHERE u.role = 'ROLE_COMPANY'
""")
    List<UserDTO> getAllCompanies();




}
