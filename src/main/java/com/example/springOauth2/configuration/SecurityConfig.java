package com.example.springOauth2.configuration;

import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DataSource dataSource;
    private final KeyManager keyManager;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain oauthSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
        httpSecurity
            .getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(
                // Customizer.withDefaults()
                customizer -> customizer.clientRegistrationEndpoint(
                    clientRegistrationEndpoint -> clientRegistrationEndpoint.authenticationProviders(CustomClientMetadataConfig.configureCustomClientMetadataConverters())
                )
            )
            .registeredClientRepository(jdbcRegisteredClientRepository())
            // .clientAuthentication(customizer -> customizer
            //     .authenticationProvider(customOauth2AuthorizationCodeAuthenticationProvider)
            // )
            // .authorizationEndpoint(customizer -> customizer.authenticationProvider(customOauth2AuthorizationCodeAuthenticationProvider))
        ;

        httpSecurity
            .formLogin(Customizer.withDefaults())
            .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()));
        return httpSecurity.build();
    }

    @Bean
    JWKSource<SecurityContext> jwkSource() {
        JWKSet jwk = new JWKSet(keyManager.rsaKey());
        return (j, sc) -> j.select(jwk);
    }

    @Bean
    JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    JdbcRegisteredClientRepository jdbcRegisteredClientRepository() {
        return new JdbcRegisteredClientRepository(new JdbcTemplate(dataSource));
    }

    @Bean
	public RegisteredClient registeredClientRepository() {
		RegisteredClient byClientId = jdbcRegisteredClientRepository().findByClientId("registrar-client");
        if (byClientId == null) {
            byClientId = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("registrar-client")
				.clientSecret("$2a$12$6x92sDs3Y7gIK.3gyx.EZeiIeqLS/Mgh1xI5yejK6ZRXbOr3sK4.i")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)	
				.scope("client.create")
				.scope("client.read")
				.build();
            jdbcRegisteredClientRepository().save(byClientId);
        }
        
		return byClientId;
	}

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        XorCsrfTokenRequestAttributeHandler csrfHandler = new XorCsrfTokenRequestAttributeHandler();
        httpSecurity
            .csrf(customizer -> customizer.csrfTokenRequestHandler(csrfHandler))
            .authorizeHttpRequests(authorize -> 
                authorize
                    .requestMatchers("/api-docs","/api-docs/*", "/swagger-ui/*").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(customizer -> Customizer.withDefaults())
            .logout(customizer -> Customizer.withDefaults())
            .exceptionHandling(customizer -> Customizer.withDefaults())
        ;
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
