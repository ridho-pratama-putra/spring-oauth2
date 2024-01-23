package com.example.springOauth2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(customizer -> customizer.disable())
            .authorizeHttpRequests(authorize -> 
                authorize
                    .requestMatchers("/api-docs","/api-docs/*", "/swagger-ui/*").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(customizer -> Customizer.withDefaults())
        ;
        return httpSecurity.build();
    }
    
}
