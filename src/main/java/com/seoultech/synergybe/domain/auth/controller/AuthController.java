package com.seoultech.synergybe.domain.auth.controller;

import com.seoultech.synergybe.domain.auth.service.AuthService;
import com.seoultech.synergybe.system.common.ApiResponse;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "소셜로그인 token api")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "access token 재발급", description = "access token, refresh token을 통해 사용자식별, 만료시간을 체크한 후 access token을 재발급합니다.")
    @GetMapping("/reissue-with-accesstoken")
    public ApiResponse reissueAccessTokenWithExpiredAccessToken (HttpServletRequest request, HttpServletResponse response) {

        return authService.reissueAccessTokenWithExpiredAccessToken(request, response);
    }

    @Operation(summary = "access token 재발급", description = "access token, refresh token을 통해 사용자식별, 만료시간을 체크한 후 access token을 재발급합니다.")
    @GetMapping("/reissue-with-refreshtoken")
    public ApiResponse reissueAccessTokenWithRefreshToken (HttpServletRequest request, HttpServletResponse response, @LoginUser String userId) {

        return authService.reissueAccessTokenWithRefreshToken(userId, request, response);
    }
}

