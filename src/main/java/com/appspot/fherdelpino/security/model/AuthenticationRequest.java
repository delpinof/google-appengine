package com.appspot.fherdelpino.security.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthenticationRequest {
    @NonNull
    String userName;
    @NonNull
    String password;
}
