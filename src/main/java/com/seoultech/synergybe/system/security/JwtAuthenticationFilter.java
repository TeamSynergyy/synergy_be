package com.seoultech.synergybe.system.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.user.dto.request.LoginRequest;
import com.seoultech.synergybe.system.apiresponse.ApiResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인, JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(),
                    LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                         FilterChain chain, Authentication authentication) throws IOException {
        String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();

        String token = jwtUtil.createToken(email);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        String result = new ObjectMapper().writeValueAsString(
                new ApiResponseDto(HttpStatus.OK.value(), "login success")
        );

        response.getOutputStream().print(result);
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
}