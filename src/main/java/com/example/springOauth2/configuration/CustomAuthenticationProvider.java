package com.example.springOauth2.configuration;

import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.springOauth2.enumeration.LDAPResult;
import com.example.springOauth2.service.CustomAppUserDetailService;

@Component
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    
    private final CustomAppUserDetailService customUserDetailService;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    // @Value("${login.ldap-port}")
    private final Integer ldapPort;

    // @Value("${login.ldap-host}")
    private final String ldapHost;
    
    // @Value("${ldap.username.suffix}")
    private final String ldapUserNameSuffix;

    CustomAuthenticationProvider(
        @Value("${login.ldap-port}")
        Integer ldapPort,

        @Value("${login.ldap-host}")
        String ldapHost,
        
        @Value("${ldap.username.suffix}")
        String ldapUserNameSuffix,
        
        CustomAppUserDetailService customUserDetailService
    ) {
        this.ldapPort = ldapPort;
        this.ldapHost = ldapHost;
        this.ldapUserNameSuffix = ldapUserNameSuffix;
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, 
            UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // super.additionalAuthenticationChecks(userDetails, authentication);
        logger.info("CustomAuthenticationProvider.additionalAuthenticationChecks :::: ");
        String principalWithLdapUserNameSuffix = userDetails.getUsername().toString().concat(ldapUserNameSuffix);
        // String credential = authentication.getCredentials().toString();
        // LDAPResult loginResult = login(principalWithLdapUserNameSuffix, credential);
        // if(!LDAPResult.SUCCESS.equals(loginResult)) {
        //     throw new BadCredentialsException(userDetails.getUsername().toString());
        // }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        logger.info("CustomAuthenticationProvider.retrieveUser :::: ");
        
        UserDetails userByUsername = this.customUserDetailService.loadUserByUsername(username);
        return userByUsername;
    }


    public LDAPResult login(String principal, String credential) {
        HashMap<String, String> environment = new HashMap<>();
        DirContext context;
        String providerUrl = "ldap://"+ ldapHost+":"+ ldapPort.toString();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, providerUrl);
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        environment.put(Context.SECURITY_PRINCIPAL, principal);
        environment.put(Context.SECURITY_CREDENTIALS, credential);

        logger.info("LDAP Login url [{}]", providerUrl);
        logger.info("LDAP Login Username [{}]", principal);

        try {
            context = new InitialDirContext(new Hashtable<>(environment));
            context.close();
            logger.info("ldap success");
            return LDAPResult.SUCCESS;
        } catch (AuthenticationException exception) {
            logger.error("AuthenticationException ::: " +exception.getMessage());
            return LDAPResult.INCORRECT_CREDENTIAL;
        } catch (NamingException exception) {
            logger.error("NamingException ::: " +exception.getMessage());
            return LDAPResult.LDAP_EXCEPTION;
        } catch (Exception exception){
            logger.error("exception ::: " +exception.getMessage());
            return LDAPResult.LDAP_EXCEPTION;
        }
    }
}
