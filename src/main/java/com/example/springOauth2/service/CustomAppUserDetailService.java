// package com.example.springOauth2.service;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Component;

// import com.example.springOauth2.model.CustomUserDetails;
// import com.example.springOauth2.repository.CustomAppUserRepository;

// import lombok.AllArgsConstructor;

// @Component
// @AllArgsConstructor
// public class CustomAppUserDetailService implements UserDetailsService {

//     final CustomAppUserRepository customAppUserRepository;
//     final Logger logger = LoggerFactory.getLogger(getClass());

//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//         logger.info("CustomAppUserDetailService :::: ");
//         CustomUserDetails result = CustomUserDetails.builder()
//             .username("username from details")
//             .build();
//         return result;
//     }
// }
