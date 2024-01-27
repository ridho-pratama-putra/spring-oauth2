package com.example.springOauth2.service;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("CustomAppUserDetailService :::: ");
        UserDetails result = User.withUsername(username)
            .password("$2a$12$IYuvkHRXvOM8yyRvMbRQV.bxW.T781yvcTD8NnOf6vjfw6q3C1nlm")
            .authorities(Arrays.asList(new SimpleGrantedAuthority("roles_1")))
            .build();
        return result;
    }
}
