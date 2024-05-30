package com.seoultech.synergybe.domain.user;

import com.seoultech.synergybe.domain.user.vo.RefreshToken;
import lombok.AccessLevel;
import lombok.Getter;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_refresh_token")
public class UserRefreshToken {
    @Id
    @Column(name = "user_refresh_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;


    @Embedded
    private RefreshToken refreshToken;

    public UserRefreshToken(
            String userId
    ) {
        this.userId = userId;
        this.refreshToken = new RefreshToken();
    }

    public void updateRefreshToken() {
        this.refreshToken = new RefreshToken();
    }
}
