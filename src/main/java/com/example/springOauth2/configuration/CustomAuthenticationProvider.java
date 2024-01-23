package com.example.springOauth2.configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object principal = authentication.getPrincipal();
        String principalWithLdapUserNameSuffix = authentication.getPrincipal().toString();
        String credential = authentication.getCredentials().toString();

        throw new AuthenticationServiceException("e.getMessage()");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
}
