package com.seoultech.synergybe.system.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.user.UserRefreshToken;
import com.seoultech.synergybe.domain.user.dto.request.LoginRequest;
import com.seoultech.synergybe.domain.user.repository.UserRefreshTokenFactory;
import com.seoultech.synergybe.domain.user.service.UserRefreshTokenReader;
import com.seoultech.synergybe.system.apiresponse.ApiResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인, JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    private final JwtUtil jwtUtil;
    private final UserRefreshTokenReader userRefreshTokenReader;
    private final UserRefreshTokenFactory userRefreshTokenFactory;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRefreshTokenReader userRefreshTokenReader, UserRefreshTokenFactory userRefreshTokenFactory) {
        this.jwtUtil = jwtUtil;
        this.userRefreshTokenReader = userRefreshTokenReader;
        this.userRefreshTokenFactory = userRefreshTokenFactory;
        setFilterProcessesUrl("/api/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(),
                    LoginRequest.class);
            List<GrantedAuthority> authorities = getAuthorities(List.of("ROLE_USER"));

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword(),
                            authorities
//                            null
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<GrantedAuthority> getAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                         FilterChain chain, Authentication authentication) throws IOException {
        UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());

        // Jwt token 생성 refresh token 생성 후 db에 저장하기
        String token = jwtUtil.createToken(userDetails.getUserId(), userDetails.getEmail());

        handleLoginSuccess(response, userDetails, token);
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request,
                                           HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        String result = new ObjectMapper().writeValueAsString(
                new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "login failure")
        );

        response.getOutputStream().print(result);
    }

    private void handleLoginSuccess(HttpServletResponse response, UserDetailsImpl userDetails, String token) throws IOException {
        UserRefreshToken userRefreshToken = getOrGenerate(userDetails.getUserId());

        // Add refresh token as a cookie
        addRefreshTokenCookie(response, userRefreshToken);

        // Add JWT token in the Authorization header
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        // Set response status and content type
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
    }

    private UserRefreshToken getOrGenerate(String userId) {
        Optional<UserRefreshToken> userRefreshToken = userRefreshTokenReader.readByUserId(userId);

        if (userRefreshToken.isPresent()) {
            userRefreshToken.get().updateRefreshToken();

            return userRefreshToken.get();
        } else {
            UserRefreshToken newUserRefreshToken = new UserRefreshToken(userId);
            userRefreshTokenFactory.save(newUserRefreshToken);

            return newUserRefreshToken;
        }
    }

    private void addRefreshTokenCookie(HttpServletResponse response, UserRefreshToken userRefreshToken) {
        String refreshToken = userRefreshToken.getRefreshToken().getRefreshToken();
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Set to true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(2 * 7 * 24 * 60 * 60); // Set expiration time if needed
        cookie.setAttribute("SameSite", "Strict"); // Can be "Lax" or "Strict" depending on your requirements
        response.addCookie(cookie);
    }
}