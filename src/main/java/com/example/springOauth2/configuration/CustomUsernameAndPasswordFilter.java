// package com.example.springOauth2.configuration;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.stereotype.Component;

// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Component
// public class CustomUsernameAndPasswordFilter extends UsernamePasswordAuthenticationFilter {

//     CustomUsernameAndPasswordFilter (AuthenticationManager authenticationManager) {
//         setAuthenticationManager(authenticationManager);
//     }

//     @Override
//     public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//         Logger logger = LoggerFactory.getLogger(getClass());
//         logger.info("CustomUsernameAndPasswordFilter ::::: ");
//         return super.attemptAuthentication(request, response);
//     }
    
// }
