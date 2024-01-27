// package com.example.springOauth2.configuration;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
// import org.springframework.stereotype.Component;

// @Component
// public class CustomOauth2AuthorizationCodeAuthenticationProvider implements AuthenticationProvider{

//     Logger logger = LoggerFactory.getLogger(getClass());

//     @Override
//     public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//         logger.info("CustomOauth2AuthorizationCodeAuthenticationProvider ::::");
//         Object principal = authentication.getPrincipal();
//         OAuth2AuthorizationCodeRequestAuthenticationToken converted = (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
    
//         // public OAuth2AuthorizationCodeRequestAuthenticationToken(String authorizationUri, String clientId, Authentication principal, @Nullable String redirectUri, @Nullable String state, @Nullable Set<String> scopes, @Nullable Map<String, Object> additionalParameters) {
//         // public OAuth2AuthorizationCodeRequestAuthenticationToken(String authorizationUri, String clientId, Authentication principal, OAuth2AuthorizationCode authorizationCode, @Nullable String redirectUri, @Nullable String state, @Nullable Set<String> scopes) {
//         OAuth2AuthorizationCodeRequestAuthenticationToken result = new OAuth2AuthorizationCodeRequestAuthenticationToken(converted.getAuthorizationUri(), converted.getClientId(), authentication, converted.getRedirectUri(), null, converted.getScopes(), null);
//         result.setAuthenticated(true);
//         return result;
//     }

//     @Override
//     public boolean supports(Class<?> authentication) {
//         return authentication.equals(OAuth2AuthorizationCodeRequestAuthenticationToken.class);
//     }
    
// }
