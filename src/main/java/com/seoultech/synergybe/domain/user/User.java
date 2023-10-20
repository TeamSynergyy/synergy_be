package com.seoultech.synergybe.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seoultech.synergybe.domain.auth.entity.ProviderType;
import com.seoultech.synergybe.domain.auth.entity.RoleType;
import com.seoultech.synergybe.domain.user.dto.request.UpdateUserRequest;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements Serializable {
    @JsonIgnore
    @Id
    @Column(name = "user_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(name = "user_id", length = 64, unique = true)
    private String userId;

    @Column(name = "username", length = 100)
    private String username;

    @JsonIgnore
    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "email", length = 512, unique = true)
    private String email;

    @Column(name = "email_verified_yn", length = 1)
    @NotNull
    private String emailVerifiedYn;

    @Column(name = "profile_image_url", length = 512)
    private String profileImageUrl;

    @Column(name = "provider_type", length = 20)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "role_type", length = 20)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(name = "create_at")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    private String major;

    private String temperature;

    private String bio;

    public User(
            String userId,
            String username,
            String email,
            String emailVerifiedYn,
            String profileImageUrl,
            ProviderType providerType,
            RoleType roleType,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.userId = userId;
        this.username = username;
        this.password = "NO_PASS";
        this.email = email != null ? email : "NO_EMAIL";
        this.emailVerifiedYn = emailVerifiedYn;
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.providerType = providerType;
        this.roleType = roleType;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.bio = "36.5";
    }

    public User update(UpdateUserRequest request) {
        this.username = request.getUsername();
        this.bio = request.getBio();
        this.major = request.getMajor();

        return this;
    }
}

