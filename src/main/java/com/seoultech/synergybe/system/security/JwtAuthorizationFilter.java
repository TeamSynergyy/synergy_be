package com.seoultech.synergybe.system.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.auth.JwtAuthenticationProvider;
import com.seoultech.synergybe.domain.user.UserRefreshToken;
import com.seoultech.synergybe.domain.user.service.UserRefreshTokenReader;
import com.seoultech.synergybe.system.apiresponse.ApiResponseDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증, 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationProvider authenticationProvider;
    private final UserRefreshTokenReader userRefreshTokenReader;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, JwtAuthenticationProvider authenticationProvider, UserRefreshTokenReader userRefreshTokenReader) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationProvider = authenticationProvider;
        this.userRefreshTokenReader = userRefreshTokenReader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Header에서 jwt 토큰 받아오기
        String tokenValue = jwtUtil.getTokenFromRequest(request);

        if (StringUtils.hasText(tokenValue)) {
            // 토큰 검증
            if (isValidate(request, response, tokenValue)) return;

            // 토큰에서 사용자 정보 가져오기
            Claims info = getClaims(response, tokenValue);
            if (info == null) return;

            // 사용자 정보 인증 객체에 담기
            if (userInfoInAuthentication(tokenValue)) return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidate(HttpServletRequest request, HttpServletResponse response, String tokenValue) throws IOException {
        // jwt 검증 진행 로직
        if (!jwtUtil.validateToken(tokenValue)) {
            // 만료되었을 경우
            // refreshToken가 존재하면 accessToken 재발급, refreshToken 재발급

            String refreshToken = getRefreshTokenFromRequest(request);

            if (refreshToken.equals("expiration")) {
                // 재로그인 요청
                return true;
            }

            String userId = userRefreshTokenReader.readUserByRefreshToken(refreshToken);
            String email = userDetailsService.getUserEmail(userId);;

            // accessToken 재발급
            String newAccessToken = jwtUtil.createToken(userId, email);


            // refreshToken 재발급
            UserRefreshToken userRefreshToken = userRefreshTokenReader.readByRefreshToken(refreshToken);
            userRefreshToken.updateRefreshToken();

            addRefreshTokenCookie(response, userRefreshToken);


            // Add JWT token in the Authorization header
            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            String result = new ObjectMapper().writeValueAsString(
                    new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "INVALID_TOKEN")
            );

            response.getOutputStream().print(result);
            return true;
        }
        return false;
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

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                String name = c.getName();
                String value = c.getValue();
                if (name.equals("refreshToken")) {
                    return value;
                }
            }
        }
        return "expiration";
    }

    private Claims getClaims(HttpServletResponse response, String tokenValue) throws IOException {
        Claims info;

        try {
            info = jwtUtil.getUserInfoFromToken(tokenValue);
        } catch (Exception e) {
            // JWT 검증에 실패한 경우 처리
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            String result = new ObjectMapper().writeValueAsString(
                    new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "INVALID_TOKEN")
            );

            response.getOutputStream().print(result);
            return null;
        }
        return info;
    }

    private boolean userInfoInAuthentication(String tokenValue) {
        try {
            setAuthentication(tokenValue);
        } catch (Exception e) {
            // 인증 처리에 실패한 경우 처리
            log.error(e.getMessage());
            return true;
        }
        return false;
    }

    // 인증 처리
    public void setAuthentication(String tokenValue) {
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        Authentication authentication = createAuthentication(account);
//        context.setAuthentication(authentication);

//        SecurityContextHolder.setContext(context);
        Authentication authentication = authenticationProvider.authenticate(tokenValue);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String account) {

        // 여기서 userId get 가능
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(account);
        String userId = userDetails.getUserId();
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}