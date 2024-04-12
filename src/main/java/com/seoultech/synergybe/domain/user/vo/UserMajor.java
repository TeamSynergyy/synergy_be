package com.seoultech.synergybe.domain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMajor {
    @Column(name = "major")
    private String major;

    public UserMajor(String value) {
        validateUserMajor(value);
        this.major = value;
    }

    private void validateUserMajor(String major) {
        if (Objects.isNull(major) || major.isBlank()) {
//            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "전공은 필수 항목입니다.");
        }
    }
}
