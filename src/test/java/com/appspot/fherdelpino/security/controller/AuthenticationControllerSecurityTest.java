package com.appspot.fherdelpino.security.controller;

import com.appspot.fherdelpino.security.configuration.WebSecurityConfig;
import com.appspot.fherdelpino.security.filter.JwtAuthFilter;
import com.appspot.fherdelpino.security.repository.UserRepository;
import com.appspot.fherdelpino.security.service.MongoAuthUserDetailsService;
import com.appspot.fherdelpino.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Proxy;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@Import({WebSecurityConfig.class, JwtAuthFilter.class, AuthenticationControllerSecurityTest.TestConfig.class})
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class AuthenticationControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void authShouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(post(AuthenticationController.AUTH_PATH)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"userName":"fernando","password":"secret"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("jwt-token"));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        UserRepository userRepository() {
            return (UserRepository) Proxy.newProxyInstance(
                    UserRepository.class.getClassLoader(),
                    new Class[]{UserRepository.class},
                    (proxy, method, args) -> {
                        if ("findById".equals(method.getName())) {
                            return Optional.empty();
                        }
                        if ("existsById".equals(method.getName())) {
                            return false;
                        }
                        if ("save".equals(method.getName())) {
                            return args[0];
                        }
                        if ("toString".equals(method.getName())) {
                            return "UserRepositoryTestStub";
                        }
                        if ("hashCode".equals(method.getName())) {
                            return System.identityHashCode(proxy);
                        }
                        if ("equals".equals(method.getName())) {
                            return proxy == args[0];
                        }
                        throw new UnsupportedOperationException(method.getName());
                    }
            );
        }

        @Bean
        @Primary
        AuthenticationManager authenticationManager() {
            return authentication -> new UsernamePasswordAuthenticationToken(
                    new User(authentication.getName(), "", AuthorityUtils.NO_AUTHORITIES),
                    null,
                    AuthorityUtils.NO_AUTHORITIES
            );
        }

        @Bean
        @Primary
        JwtUtils jwtUtils() {
            return new JwtUtils() {
                @Override
                public String generateJwtToken(org.springframework.security.core.Authentication authentication) {
                    return "jwt-token";
                }

                @Override
                public boolean validateJwtToken(String authToken) {
                    return false;
                }
            };
        }

        @Bean
        @Primary
        MongoAuthUserDetailsService mongoAuthUserDetailsService() {
            return new MongoAuthUserDetailsService() {
                @Override
                public UserDetails loadUserByUsername(String username) {
                    return new User(username, "", AuthorityUtils.NO_AUTHORITIES);
                }
            };
        }
    }
}
