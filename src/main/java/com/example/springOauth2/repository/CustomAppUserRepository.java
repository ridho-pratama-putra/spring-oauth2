package com.example.springOauth2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springOauth2.model.CustomUserDetails;

public interface CustomAppUserRepository extends JpaRepository<CustomUserDetails, Long>{
    Optional<CustomUserDetails> findByUsername(String username);
}
