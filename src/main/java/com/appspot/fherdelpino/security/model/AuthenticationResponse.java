package com.appspot.fherdelpino.security.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthenticationResponse {
    String jwt;
}
