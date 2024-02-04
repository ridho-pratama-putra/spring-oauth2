package com.example.springOauth2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springOauth2.model.CustomUserDetail;

@Service
public class CustomAppUserDetailService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("CustomAppUserDetailService.loadUserByUsername ::: ");
        UserDetails userDetails = CustomUserDetail.builder()
            .username(username)
            .build();
        return userDetails;
    }

}
