package com.appspot.fherdelpino.security.controller;

import com.appspot.fherdelpino.security.error.UserConflictException;
import com.appspot.fherdelpino.security.model.AuthenticationRequest;
import com.appspot.fherdelpino.security.model.AuthenticationResponse;
import com.appspot.fherdelpino.security.model.Role;
import com.appspot.fherdelpino.security.model.User;
import com.appspot.fherdelpino.security.repository.UserRepository;
import com.appspot.fherdelpino.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthenticationController {

    public static final String AUTH_PATH = "/auth";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(AUTH_PATH)
    @ResponseBody
    public AuthenticationResponse auth(@RequestBody AuthenticationRequest request) {
        var token = new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        String jwt = jwtUtils.generateJwtToken(authentication);
        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }

    @PostMapping(AUTH_PATH + "/signup")
    @ResponseBody
    public User signup(@RequestBody AuthenticationRequest request) {
        if (userRepository.existsById(request.getUserName())) {
            throw new UserConflictException("User already exists");
        }
        return userRepository.save(User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(Role.USER))
                .build());
    }

}
