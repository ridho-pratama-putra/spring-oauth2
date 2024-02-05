package com.example.springOauth2.configuration;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final KeyManager keyManager;
    private final DataSource dataSource;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    JdbcOAuth2AuthorizationService jdbcOAuth2AuthorizationService() {
        CustomOauth2AuthorizationService result = new CustomOauth2AuthorizationService(jdbcTemplate(), jdbcRegisteredClientRepository());
        return result;
    }

    @Bean
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
            .authorizationService(jdbcOAuth2AuthorizationService())
        ;

        httpSecurity
            .formLogin(Customizer.withDefaults())
            .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()))
            ;
        return httpSecurity.build();
    }

    @Bean
    JWKSource<SecurityContext> jwkSource() {
        JWKSet jwk = new JWKSet(keyManager.rsaKey());
        return new JWKSource<SecurityContext>() {
            @Override
            public List<JWK> get(JWKSelector arg0, SecurityContext arg1) throws KeySourceException {
                return arg0.select(jwk);
            }
        };
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
	RegisteredClient registeredClientRepository() {
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
        // XorCsrfTokenRequestAttributeHandler csrfHandler = new XorCsrfTokenRequestAttributeHandler();
        httpSecurity
            // .csrf(customizer -> customizer.csrfTokenRequestHandler(csrfHandler))
            .csrf(customizer -> customizer.disable())
            .authorizeHttpRequests(authorize -> 
                authorize
                    .requestMatchers("/api-docs","/api-docs/*", "/swagger-ui/*").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(customizer -> Customizer.withDefaults())
            // .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(customAuthenticationProvider)
        ;

        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
	OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() { 
		return (context) -> {
			if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) { 
				context.getClaims().claims((claims) -> {
					Set<String> roles = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities())
							.stream()
							.map(c -> c.replaceFirst("^ROLE_", ""))
							.collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet)); 
					claims.put("roles", roles); 
				});
			}
		};
	}
}
