package com.seoultech.synergybe.domain.user.repository;

import com.seoultech.synergybe.domain.user.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    Optional<UserRefreshToken> findByUserId(String userId);
    UserRefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);

    UserRefreshToken findByRefreshToken(String token);
}
