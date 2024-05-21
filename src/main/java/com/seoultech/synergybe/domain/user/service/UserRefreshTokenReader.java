package com.seoultech.synergybe.domain.user.service;

import com.seoultech.synergybe.domain.user.UserRefreshToken;
import com.seoultech.synergybe.domain.user.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRefreshTokenReader {
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    public Optional<UserRefreshToken> readByUserId(String userId) {
        return userRefreshTokenRepository.findByUserId(userId);
    }

    public UserRefreshToken readByRefreshToken(String token) {
        return userRefreshTokenRepository.findByRefreshTokenRefreshToken(token);
    }

    public String readUserByRefreshToken(String token) {
        return userRefreshTokenRepository.findUserIdByRefreshTokenRefreshToken(token);
    }
}
