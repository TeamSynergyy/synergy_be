package com.seoultech.synergybe.domain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Getter
@Embeddable
public class RefreshToken {
    @Column(name = "refresh_token")
    private String refreshToken;

    public RefreshToken() {
        this.refreshToken = String.valueOf(UUID.randomUUID());
    }
}
