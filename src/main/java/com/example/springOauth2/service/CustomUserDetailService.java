package com.example.springOauth2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.springOauth2.model.CustomUserDetails;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // AbstractUserDetailsAuthenticationProvider
        logger.info("CustomAppUserDetailService.loadUserByUsername :::: ");
        UserDetails result = CustomUserDetails.builder()
            .email("email")
            .username("username")
            .isActive(true)
        .build();
        return result;
    }
}
