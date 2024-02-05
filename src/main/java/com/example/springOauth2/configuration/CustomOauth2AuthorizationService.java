package com.example.springOauth2.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

public class CustomOauth2AuthorizationService extends JdbcOAuth2AuthorizationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public CustomOauth2AuthorizationService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository) {
        super(jdbcOperations, registeredClientRepository);
    }

    @Override
    @Nullable
    public OAuth2Authorization findById(String id) {
        logger.info("CustomOauth2AuthorizationService.findById ::: ");
        return super.findById(id);
    }

    @Override
    @Nullable
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        return super.findByToken(token, tokenType);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        logger.info("CustomOauth2AuthorizationService.remove ::: ");
        super.remove(authorization);
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        logger.info("CustomOauth2AuthorizationService.save ::: ");
        super.save(authorization);
    }
    
}
