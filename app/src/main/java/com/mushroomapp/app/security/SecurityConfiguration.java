package com.mushroomapp.app.security;

import com.google.cloud.spring.security.firebase.FirebaseJwtTokenDecoder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

//    @Bean
//    JwtDecoder jwtDecoder() {
//        return (String jwt) -> {
////            FirebaseJwtTokenDecoder decoder = new FirebaseJwtTokenDecoder(
////            );
//            try {
//                FirebaseToken token = FirebaseAuth
//                        .getInstance()
//                        .verifyIdToken(jwt);
//
//
//                Map<String, Object> mapped = new HashMap<>();
//
//                token.getClaims().forEach((a,b) -> {
//                    System.out.println("a="+a +", b="+b);
//                    if(b instanceof Long) mapped.put(
//                            a, Instant.ofEpochMilli((Long)b)
//                    );
//                    else mapped.put(a,b);
//                });
//
//                System.out.println("Got token " + token);
//
//                return Jwt
//                        .withTokenValue(token.toString())
//                        .claims(c -> c.putAll(mapped))
//                        .build();
//
//            } catch (FirebaseAuthException e) {
//                throw new RuntimeException(e);
//            }
//        };eeeeeeee
//    }
//
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(o -> o.jwt(jwt -> jwt.jwkSetUri("https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com")))
                .build();
    }
}