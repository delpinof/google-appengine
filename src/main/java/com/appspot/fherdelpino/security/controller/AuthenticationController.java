package com.appspot.fherdelpino.security.controller;

import com.appspot.fherdelpino.security.model.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    public static final String AUTH_PATH = "/auth";

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(AUTH_PATH)
    @ResponseBody
    public String auth(@RequestBody AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        return "authorized";
    }

}
