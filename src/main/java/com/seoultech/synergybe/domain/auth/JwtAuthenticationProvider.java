package com.seoultech.synergybe.domain.auth;

import com.seoultech.synergybe.domain.auth.dto.CustomClaims;
import com.seoultech.synergybe.system.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {
    private final JwtUtil jwtUtil;

    public Authentication authenticate(String accessToken) {
        CustomClaims claims = jwtUtil.getUserId(accessToken);
        JwtAuthentication authentication = new JwtAuthentication(claims.userId(), accessToken);
        List<GrantedAuthority> authorities = getAuthorities(claims.authorities());
        return UsernamePasswordAuthenticationToken.authenticated(authentication, accessToken, authorities);
    }

    private List<GrantedAuthority> getAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();
    }
}
