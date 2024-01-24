package com.example.springOauth2.configuration;

import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
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
                clientRegistrationEndpoint -> clientRegistrationEndpoint.authenticationProviders(CustomClientMetadataConfig.configureCustomClientMetadataConverters())
            )
        )
        .registeredClientRepository(jdbcRegisteredClientRepository());

        httpSecurity.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()));
        return httpSecurity.build();
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
		RegisteredClient registrarClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("registrar-client")
				.clientSecret("{noop}secret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)	
				.scope("client.create")	
				.scope("client.read")
				.build();
        
        jdbcRegisteredClientRepository().save(registrarClient);
		return registrarClient;
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
        ;
        return httpSecurity.build();
    }
    
}
