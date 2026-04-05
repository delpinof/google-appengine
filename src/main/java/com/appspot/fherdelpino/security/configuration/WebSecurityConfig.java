package com.appspot.fherdelpino.security.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                        .requestMatchers("/hello").permitAll()
                        .requestMatchers("/ai-steen/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/**").permitAll()
                        .requestMatchers("/expense/**").permitAll()//.hasAuthority("USER")
                        .requestMatchers("/restaurant/**").permitAll()//.hasAuthority("USER")
                        .anyRequest().authenticated()
                )
                .oauth2Login(login -> login
                        .defaultSuccessUrl("/app", true)
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> {
                        })
                );
        return http.build();
    }
}
