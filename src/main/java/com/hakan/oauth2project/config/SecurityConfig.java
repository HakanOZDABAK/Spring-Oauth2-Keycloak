package com.hakan.oauth2project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        DelegatingJwtGrantedAuthoritiesConverter authoritiesConverter =
                // Using the delegating converter multiple converters can be combined
                new DelegatingJwtGrantedAuthoritiesConverter(
                        // First add the default converter
                        new JwtGrantedAuthoritiesConverter(),
                        // Second add our custom Keycloak specific converter
                        new KeycloakJwtRolesConverter());

        // Set up http security to use the JWT converter defined above
        httpSecurity.oauth2ResourceServer().jwt().jwtAuthenticationConverter(
                jwt -> new JwtAuthenticationToken(jwt, authoritiesConverter.convert(jwt)));
       return httpSecurity.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/home/**"))
               . authorizeHttpRequests(authorize -> authorize
                       .requestMatchers("/api/home/user").hasAuthority(KeycloakJwtRolesConverter.PREFIX_RESOURCE_ROLE + "oauth2-demo-pcke-client_user")
                       .requestMatchers("/api/home/**").hasAuthority(KeycloakJwtRolesConverter.PREFIX_RESOURCE_ROLE + "oauth2-demo-pcke-client_admin")
                       .anyRequest().authenticated())
                       .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                       .oauth2ResourceServer(oauth2ResourceServer ->oauth2ResourceServer.jwt(Customizer.withDefaults()))
                       .build();
    }
    }

