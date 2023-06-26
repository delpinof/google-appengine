package com.appspot.fherdelpino.security.configuration;


import com.appspot.fherdelpino.security.filter.JwtAuthFilter;
import com.appspot.fherdelpino.security.service.MongoAuthUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

import static com.appspot.fherdelpino.security.controller.AuthenticationController.AUTH_PATH;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String ENCODE_ID = "bcrypt";

    @Autowired
    private MongoAuthUserDetailsService mongoAuthUserDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/hello").permitAll()
                .requestMatchers(AUTH_PATH + "/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/**").permitAll()
                .requestMatchers("/expense/**").permitAll()//.hasAuthority("USER")
                .anyRequest().authenticated()
        ).csrf().disable();
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(mongoAuthUserDetailsService);
        return builder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(ENCODE_ID, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(ENCODE_ID, encoders);
    }
}
