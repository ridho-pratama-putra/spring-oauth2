package com.example.springOauth2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springOauth2.repository.CustomAppUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomAppUserDetailService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CustomAppUserRepository customUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // AbstractUserDetailsAuthenticationProvider
        logger.info("CustomAppUserDetailService.loadUserByUsername :::: ");
        UserDetails result = customUserRepository.findByUsername(username).orElse(null);
        return result;
    }
}
