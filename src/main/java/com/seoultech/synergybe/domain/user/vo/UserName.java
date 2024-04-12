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
public class UserName {
    private static final int MAX_USER_NAME_LENGTH = 10;

    @Column(name = "name")
    private String name;

    public UserName(String value) {
        validateNotNull(value);
        validateUserNameLength(value);
        this.name = value;
    }

    private void validateNotNull(String name) {
        if (Objects.isNull(name) || name.isBlank()) {
//            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "이름은 필수 항목입니다.");
        }
    }

    private void validateUserNameLength(String name) {
        if (name.length() > MAX_USER_NAME_LENGTH) {
//            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "이름은 최대 10자입니다.");
        }
    }

    public UserName updateName(String name) {
        return name != null ? new UserName(name) : this;
    }
}
