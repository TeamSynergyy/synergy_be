package com.seoultech.synergybe.domain.user.vo;

import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPassword {
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 16;

    @Column(name = "password")
    private String password;

    public UserPassword(String rawPassword, CustomPasswordEncoder passwordEncoder) {
        validateNotNull(rawPassword);
        validateUserPasswordLength(rawPassword);
        this.password = passwordEncoder.encode(rawPassword);
    }

    private void validateNotNull(String password) {
        if (Objects.isNull(password)) {
//            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "패스워드는 필수값입니다.");
        }
    }

    private void validateUserPasswordLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
//            throw new UserBadRequestException(ErrorCode.BAD_REQUEST,
//                    MessageFormat.format("패스워드는 {0}자 이상, {1}자 이하여야 합니다.",
//                            MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH));
        }
    }

}
