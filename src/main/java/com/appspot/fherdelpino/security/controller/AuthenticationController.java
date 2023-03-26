package com.appspot.fherdelpino.security.controller;

import com.appspot.fherdelpino.security.error.UserAlreadyExistException;
import com.appspot.fherdelpino.security.model.AuthenticationRequest;
import com.appspot.fherdelpino.security.model.Role;
import com.appspot.fherdelpino.security.model.User;
import com.appspot.fherdelpino.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final String AUTH_PATH = "/auth";

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(AUTH_PATH)
    @ResponseBody
    public String auth(@RequestBody AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        return "authorized";
    }

    @PostMapping(AUTH_PATH + "/signup")
    @ResponseBody
    public User signup(@RequestBody AuthenticationRequest request) {
        if (userRepository.existsById(request.getUserName())) {
            throw new UserAlreadyExistException("User already exists");
        }
        return userRepository.save(User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(Role.USER))
                .build());
    }

}
