package com.seoultech.synergybe.domain.user.vo;

import com.seoultech.synergybe.domain.user.exception.UserBadRequestException;
import com.seoultech.synergybe.system.exception.ErrorCode;
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
        if (major == null || major.isBlank()) {
            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "전공은 필수 항목입니다.");
        }
    }

    public UserMajor updateMajor(String major) {
        return major != null ? new UserMajor(major) : this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMajor userMajor = (UserMajor) o;
        return Objects.equals(major, userMajor.major);
    }

    @Override
    public int hashCode() {
        return Objects.hash(major);
    }
}
