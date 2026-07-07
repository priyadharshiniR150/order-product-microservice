package com.examle.user_service.SecurityConfig;



import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorizationServerConfig {

    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKeyProperties keys) {

    	RSAKey rsaKey = new RSAKey.Builder(keys.getPublicKey())
    	        .privateKey(keys.getPrivateKey())
    	        .keyID("user-service-key")
    	        .build();
        JWKSet jwkSet = new JWKSet(rsaKey);

        return new ImmutableJWKSet<>(jwkSet);
    }
}