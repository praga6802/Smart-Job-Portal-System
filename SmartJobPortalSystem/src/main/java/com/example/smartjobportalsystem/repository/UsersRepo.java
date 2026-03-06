package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.dto.UserDTO;
import com.example.smartjobportalsystem.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsersRepo extends JpaRepository<Users,Integer> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByMobNumber(String mobNumber);


    @Query("SELECT new com.example.smartjobportalsystem.dto.UserDTO(u.id, u.username, u.email, u.role, u.mobNumber) " +
            "FROM Users u WHERE u.role = 'ROLE_ADMIN'")
    List<UserDTO> getAllAdmins();

    @Query("SELECT new com.example.smartjobportalsystem.dto.UserDTO(u.id, u.username, u.email, u.role,u.mobNumber)"+
    "FROM Users u WHERE u.role = 'ROLE_USER'")
    List<UserDTO> getAllUsers();

    @Query("SELECT new com.example.smartjobportalsystem.dto.UserDTO(u.id, u.username, u.email, u.role,u.mobNumber)"+
    "FROM Users u WHERE u.role = 'ROLE_COMPANY'")
    List<UserDTO> getAllCompanies();

}
