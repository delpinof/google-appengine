package com.appspot.fherdelpino;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class GoogleAppengineApplicationTests {

	@MockitoBean
	JwtDecoder jwtDecoder;

	@MockitoBean
	ClientRegistrationRepository clientRegistrationRepository;

	@MockitoBean
	OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

	@Test
	void contextLoads() {
	}

}
