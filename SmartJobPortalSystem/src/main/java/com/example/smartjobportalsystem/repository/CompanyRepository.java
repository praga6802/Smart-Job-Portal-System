package com.example.smartjobportalsystem.repository;

import com.example.smartjobportalsystem.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer> {
    boolean existsByEmail(String email);

    boolean existsByContact(String contact);

    boolean existsByUrl(String url);

    boolean existsByGst(String gst);

    Optional<Company> findByEmail(String email);

    Optional<Company> findByUser_UserId(Integer userId);

    Optional<Company> findByName(String companyName);


    @Query("SELECT COUNT(c) FROM Company c")
    Long getTotalCompanies();
}