package com.example.springOauth2.configuration;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import com.example.springOauth2.model.CustomUserDetails;
import com.example.springOauth2.service.JwtService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = authentication.getPrincipal().toString();
        String principalWithLdapUserNameSuffix = authentication.getPrincipal().toString();
        String credential = authentication.getCredentials().toString();
        UserDetails user;

        try {
            user = userDetailsService.loadUserByUsername(principal);
        } catch (NoSuchElementException exception) {
            throw new PreAuthenticatedCredentialsNotFoundException(exception.getMessage());
        }

        String jwtToken;
        String refreshToken;

         try {
            jwtToken = jwtService.generateToken(user);
            refreshToken = jwtService.generateRefreshToken(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationServiceException(e.getMessage());
        }

        CustomUserDetails customUserDetails = CustomUserDetails.builder()
        .username("value from CustomAuthenticationProvider")
        .build();

        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> claims = new HashMap<>();

        Jwt jwt = new Jwt(jwtToken, Instant.now(), Instant.now(), headers, claims);
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(jwt, customUserDetails.getAuthorities());
        return jwtAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
}
