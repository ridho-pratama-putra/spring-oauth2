package com.example.springOauth2.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DataSource dataSource;

    @Bean 
	SecurityFilterChain oauthSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(
            customizer -> customizer.clientRegistrationEndpoint(
                clientRegistrationEndpoint -> Customizer.withDefaults()
            )
        );	// Enable OpenID Connect 1.0
        return httpSecurity.build();
    }

    @Bean
    JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    RegisteredClientRepository registeredClientRepository() {
        JdbcRegisteredClientRepository clientRepository = new JdbcRegisteredClientRepository(jdbcTemplate());

        return clientRepository;
    }

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
