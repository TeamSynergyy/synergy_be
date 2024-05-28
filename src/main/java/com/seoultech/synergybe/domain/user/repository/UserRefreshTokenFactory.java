package com.seoultech.synergybe.domain.user.repository;

import com.seoultech.synergybe.domain.user.UserRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class UserRefreshTokenFactory {
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    public void save(UserRefreshToken userRefreshToken) {
        userRefreshTokenRepository.save(userRefreshToken);
    }
}
