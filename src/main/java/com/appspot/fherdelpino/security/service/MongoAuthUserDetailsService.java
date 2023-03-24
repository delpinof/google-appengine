package com.appspot.fherdelpino.security.service;

import com.appspot.fherdelpino.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MongoAuthUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .map(mongoUser -> new User(mongoUser.getUserName(), mongoUser.getPassword(),
                        mongoUser.getRoles().stream()
                                .map(Enum::name)
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()))
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

    }
}
