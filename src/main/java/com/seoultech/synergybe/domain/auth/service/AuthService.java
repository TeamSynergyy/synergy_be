package com.seoultech.synergybe.domain.auth.service;

import com.seoultech.synergybe.domain.auth.entity.AuthReqModel;
import com.seoultech.synergybe.domain.auth.entity.RoleType;
import com.seoultech.synergybe.domain.auth.entity.UserPrincipal;
import com.seoultech.synergybe.domain.auth.token.AuthToken;
import com.seoultech.synergybe.domain.auth.token.AuthTokenProvider;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.UserRefreshToken;
import com.seoultech.synergybe.domain.user.repository.UserRefreshTokenRepository;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.common.ApiResponse;
import com.seoultech.synergybe.system.config.properties.AppProperties;
import com.seoultech.synergybe.system.utils.CookieUtil;
import com.seoultech.synergybe.system.utils.HeaderUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserService userService;


    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    public ApiResponse reissueAccessTokenWithExpiredAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
        if (!authToken.validate()) {
            return ApiResponse.invalidAccessToken();
        }

        // expired access token 인지 확인
        Claims claims = authToken.getExpiredTokenClaims();
        if (claims == null) {
            return ApiResponse.notExpiredTokenYet();
        }

        String userId = claims.getSubject();
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        return reissueAccessToken(userId, request, response, roleType);
    }

    public ApiResponse reissueAccessTokenWithRefreshToken(String userId, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getUser(userId);
        RoleType roleType = user.getRoleType();

        // refresh token
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));

        // userId 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
            return ApiResponse.invalidRefreshToken();
        }

        if (!refreshToken.equals(userRefreshToken.getRefreshToken())) {
            return ApiResponse.invalidRefreshToken();
        }

        return reissueAccessToken(userId, request, response, roleType);
    }

    public ApiResponse reissueAccessToken(String userId, HttpServletRequest request, HttpServletResponse response,
                                          RoleType roleType) {

        Date now = new Date();

        // refresh token
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));

        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        // refresh token 검증
        if (!authRefreshToken.validate()) {
            return ApiResponse.invalidRefreshToken();
        }

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
            return ApiResponse.invalidRefreshToken();
        }

        // access token 재발급
        AuthToken newAccessToken = createAccessToken(userId, roleType, now);

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        if (validTime <= THREE_DAYS_MSEC) {
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
            reissueRefreshToken(validTime, now, userRefreshToken, refreshTokenExpiry);

            int cookieMaxAge = (int) refreshTokenExpiry / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
        }

        return ApiResponse.success("token", newAccessToken.getToken());
    }

    // access token 생성
    private AuthToken createAccessToken(String userId, RoleType roleType, Date now) {
        return tokenProvider.createAuthToken(
                userId,
                roleType.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );
    }

    // refresh token 생성
    private AuthToken createRefreshToken(Date now) {
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        return tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );
    }

    private void checkRefreshToken(String userId, AuthToken refreshToken) {
        // userId 로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userId);
        if (userRefreshToken == null) {
            // 없는 경우 새로 등록
            userRefreshToken = new UserRefreshToken(userId, refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        } else {
            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        }
    }

    public void reissueRefreshToken(long validTime, Date now, UserRefreshToken userRefreshToken, long refreshTokenExpiry) {

        if (validTime <= THREE_DAYS_MSEC) {
            // refresh 토큰 설정
            AuthToken authRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );
            log.info("refresh reissue >> " + authRefreshToken.getToken());

            // DB에 refresh 토큰 업데이트
            userRefreshTokenRepository.save(userRefreshToken.updateRefreshToken(authRefreshToken.getToken()));
        }
    }

}
