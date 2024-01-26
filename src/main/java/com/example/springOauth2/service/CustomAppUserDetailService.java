package com.example.springOauth2.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springOauth2.model.CustomUserDetails;
import com.example.springOauth2.repository.CustomAppUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomAppUserDetailService implements UserDetailsService {

    final CustomAppUserRepository customAppUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails result = CustomUserDetails.builder()
        .username("username from details")
        .build();
        return result;
    }
}
