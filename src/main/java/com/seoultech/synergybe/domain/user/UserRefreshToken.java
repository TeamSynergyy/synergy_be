package com.seoultech.synergybe.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_refresh_token")
public class UserRefreshToken {
    @Id
    @Column(name = "user_refresh_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "refresh_token", length = 256)
    private String refreshToken;

    public UserRefreshToken(
            String userId,
            String refreshToken
    ) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public UserRefreshToken updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
