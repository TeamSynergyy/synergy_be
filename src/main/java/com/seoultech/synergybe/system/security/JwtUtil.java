package com.seoultech.synergybe.system.security;

import com.seoultech.synergybe.domain.auth.dto.CustomClaims;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization"; // Header KEY 값
    public static final String BEARER_PREFIX = "Bearer "; // Token 식별자
    private static final long TOKEN_TIME = Duration.ofHours(5).toMillis(); // 토큰 만료시간 5hours

    @Value("${jwt.secret}") // Base 64 decode시 사용하는 Key
    private String secretKey;
    private Key key;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String userId, String email) {
        Date date = new Date();
//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email) // 사용자 식별
                        .claim("id", userId)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료시간
                        .setIssuedAt(date) // 발급날짜
                        .signWith(key, signatureAlgorithm) // 암호화시 사용하는 알고리즘
                        .compact();
    }

    // HttpServletRequest의 Header에 있는 JWT
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

//    public CustomClaims parseAccessToken(String token) {
//        try {
//            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
//                    .getBody();
//            String userId = claims.getSubject();
//        }
//    }

    // 토큰의 사용자 정보
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody();
    }

    public CustomClaims getUserId(String token) {
        Claims claims = getUserInfoFromToken(token);

        return new CustomClaims(claims.get("id", String.class), List.of("ROLE_USER"));
    }

}
