package com.examle.user_service.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examle.user_service.SecurityConfig.RSAKeyProperties;
import com.nimbusds.jose.jwk.RSAKey;

import java.util.Map;

@RestController
public class JwksController {

    @Autowired
    private RSAKeyProperties keys;

    @GetMapping("/oauth2/jwks")
    public Map<String, Object> keys() {

        RSAKey key = new RSAKey.Builder(keys.getPublicKey())
                .keyID("user-service-key")
                .build();

        return Map.of("keys", new Object[]{
                key.toPublicJWK().toJSONObject()
        });
    }
}