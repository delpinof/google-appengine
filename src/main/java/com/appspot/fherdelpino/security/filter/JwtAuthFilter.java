package com.appspot.fherdelpino.security.filter;

import com.appspot.fherdelpino.security.service.MongoAuthUserDetailsService;
import com.appspot.fherdelpino.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";
    private static final String BLANK_REGEX = "\\p{Zs}+";

    @Autowired
    private MongoAuthUserDetailsService mongoAuthUserDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasLength(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String jwt = authorizationHeader.split(BLANK_REGEX)[1];
            boolean isValid = jwtUtils.validateJwtToken(jwt);
            if (isValid) {
                String userName = jwtUtils.getUserName(jwt);
                UserDetails userDetails = mongoAuthUserDetailsService.loadUserByUsername(userName);
                var token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);

            }
        }
        filterChain.doFilter(request, response);
    }
}
