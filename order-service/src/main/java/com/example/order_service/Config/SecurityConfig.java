package com.example.order_service.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {

    @Autowired
    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())

        .authorizeHttpRequests(auth -> auth

        	    // ================= CUSTOMER =================

        	    .requestMatchers("/cart/**")
        	    .hasRole("CUSTOMER")

        	    .requestMatchers("/address/**")
        	    .hasRole("CUSTOMER")

        	    .requestMatchers(HttpMethod.POST, "/orders/placeOrder")
        	    .hasRole("CUSTOMER")

        	    .requestMatchers(HttpMethod.POST, "/orders/buyNow")
        	    .hasRole("CUSTOMER")

        	    .requestMatchers(HttpMethod.PUT, "/orders/cancel/**")
        	    .hasRole("CUSTOMER")

        	    .requestMatchers(HttpMethod.GET, "/orders/{id}")
        	    .hasRole("CUSTOMER")



        	    // ================= ADMIN =================

        	    .requestMatchers(HttpMethod.GET, "/orders/list")
        	    .hasRole("ADMIN")

        	    .requestMatchers(HttpMethod.PUT, "/orders/confirm/**")
        	    .hasRole("ADMIN")

        	    .requestMatchers(HttpMethod.PUT, "/orders/pack/**")
        	    .hasRole("ADMIN")

        	    .requestMatchers(HttpMethod.PUT, "/orders/ship/**")
        	    .hasRole("ADMIN")

        	    .requestMatchers(HttpMethod.PUT, "/orders/out-for-delivery/**")
        	    .hasRole("ADMIN")

        	    .requestMatchers(HttpMethod.PUT, "/orders/deliver/**")
        	    .hasRole("ADMIN")

        	    .requestMatchers(HttpMethod.DELETE, "/orders/**")
        	    .hasRole("ADMIN")

        	    .anyRequest().authenticated()
        	)

            .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwt ->
                            jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }
}