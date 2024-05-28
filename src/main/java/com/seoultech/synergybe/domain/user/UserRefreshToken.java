package com.seoultech.synergybe.domain.user;

import com.seoultech.synergybe.domain.user.vo.RefreshToken;
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


    @Embedded
    private RefreshToken refreshToken;

    public UserRefreshToken(
            String userId
    ) {
        this.userId = userId;
        this.refreshToken = new RefreshToken();
    }

    public UserRefreshToken updateRefreshToken() {
        this.refreshToken = new RefreshToken();
        return this;
    }
}
