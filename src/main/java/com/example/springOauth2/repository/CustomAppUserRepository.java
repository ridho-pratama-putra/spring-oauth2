package com.example.springOauth2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springOauth2.model.CustomAppUserDetails;

@Repository
public interface CustomAppUserRepository extends JpaRepository<CustomAppUserDetails, Long>{
    Optional<CustomAppUserDetails> findByUsername(String username);
}
