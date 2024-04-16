package com.seoultech.synergybe.system.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.system.apiresponse.ApiResponseDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증, 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Header에서 jwt 토큰 받아오기
        String tokenValue = jwtUtil.getTokenFromRequest(request);

        if (StringUtils.hasText(tokenValue)) {
            // 토큰 검증
            if (notValidate(response, tokenValue)) return;

            // 토큰에서 사용자 정보 가져오기
            Claims info = getClaims(response, tokenValue);
            if (info == null) return;

            // 사용자 정보 인증 객체에 담기
            if (userInfoInAuthentication(info)) return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean notValidate(HttpServletResponse res, String tokenValue) throws IOException {
        if (!jwtUtil.validateToken(tokenValue)) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.setContentType("application/json");
            String result = new ObjectMapper().writeValueAsString(
                    new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "INVALID_TOKEN")
            );

            res.getOutputStream().print(result);
            return true;
        }
        return false;
    }

    private Claims getClaims(HttpServletResponse res, String tokenValue) throws IOException {
        Claims info;

        try {
            info = jwtUtil.getUserInfoFromToken(tokenValue);
        } catch (Exception e) {
            // JWT 검증에 실패한 경우 처리
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.setContentType("application/json");
            String result = new ObjectMapper().writeValueAsString(
                    new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "INVALID_TOKEN")
            );

            res.getOutputStream().print(result);
            return null;
        }
        return info;
    }

    private boolean userInfoInAuthentication(Claims info) {
        try {
            setAuthentication(info.getSubject());
        } catch (Exception e) {
            // 인증 처리에 실패한 경우 처리
            log.error(e.getMessage());
            return true;
        }
        return false;
    }

    // 인증 처리
    public void setAuthentication(String account) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(account);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String account) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(account);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}