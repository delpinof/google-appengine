package com.appspot.fherdelpino.security.configuration;


import com.appspot.fherdelpino.security.service.MongoAuthUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static com.appspot.fherdelpino.security.controller.AuthenticationController.AUTH_PATH;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    MongoAuthUserDetailsService mongoAuthUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                .requestMatchers("/hello").permitAll()
                .requestMatchers(AUTH_PATH).permitAll()
                .requestMatchers("/expenses/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        ).csrf().disable();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(mongoAuthUserDetailsService);
        return builder.build();
    }
}
