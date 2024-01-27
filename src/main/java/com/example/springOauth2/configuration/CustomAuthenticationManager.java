// package com.example.springOauth2.configuration;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.stereotype.Component;

// import lombok.AllArgsConstructor;

// @Component
// @AllArgsConstructor
// public class CustomAuthenticationManager implements AuthenticationManager {

//     /*
//      * An AuthenticationManager can do one of 3 things in its authenticate() method:
//      * Return an Authentication (normally with authenticated=true) if it can verify that the input represents a valid principal.
//      * Throw an AuthenticationException if it believes that the input represents an invalid principal.
//      * Return null if it cannot decide.
//      */

//      final CustomAuthenticationProvider customAuthenticationProvider;


//     @Override
//     public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//         Logger logger = LoggerFactory.getLogger(getClass());
//         logger.info("CustomAuthenticationManager ::::");
//         return customAuthenticationProvider.authenticate(authentication);
//     }
    
// }
