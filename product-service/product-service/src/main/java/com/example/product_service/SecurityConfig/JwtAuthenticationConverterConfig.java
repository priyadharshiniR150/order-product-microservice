package com.example.product_service.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class JwtAuthenticationConverterConfig {

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter(){

	    JwtGrantedAuthoritiesConverter converter =
	            new JwtGrantedAuthoritiesConverter();

	    converter.setAuthoritiesClaimName("role"); // change roles -> role
	    converter.setAuthorityPrefix("ROLE_");

	    JwtAuthenticationConverter jwtConverter =
	            new JwtAuthenticationConverter();

	    jwtConverter.setJwtGrantedAuthoritiesConverter(converter);

	    return jwtConverter;
	}}