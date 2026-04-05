package com.appspot.fherdelpino.palg.configuration;

import com.appspot.fherdelpino.error.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class TenantContext {

    public String getTenantId() {
        Map<String, Object> claims = getClaims();
        return (String) Optional.ofNullable(claims.get("custom:tenant_id"))
                .or(() -> Optional.ofNullable(claims.get("tenant_id")))
                .or(() -> Optional.ofNullable(claims.get("cognito:username")))
                .or(() -> Optional.ofNullable(claims.get("username")))
                .orElseThrow(() -> new UnauthorizedException("Tenant not found in JWT"));
    }

    public String getUserId() {
        Map<String, Object> claims = getClaims();
        return String.valueOf(claims.get("sub"));
    }

    public String getUserName() {
        Map<String, Object> claims = getClaims();
        return (String) Optional.ofNullable(claims.get("cognito:username"))
                .or(() -> Optional.ofNullable(claims.get("username")))
                .orElseThrow(() -> new UnauthorizedException("Tenant not found in JWT"));
    }

    private Map<String, Object> getClaims() {
        Object principal = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .orElseThrow(() -> new UnauthorizedException("Principal not found"));

        if (principal instanceof DefaultOidcUser defaultOidcUser) {
            return defaultOidcUser.getClaims();
        }
        if (principal instanceof Jwt jwt) {
            return jwt.getClaims();
        }

        throw new UnauthorizedException("Unsupported principal type");
    }
}

