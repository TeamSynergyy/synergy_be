package com.seoultech.synergybe.domain.user.vo;

import com.seoultech.synergybe.domain.user.exception.UserBadRequestException;
import com.seoultech.synergybe.system.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTemperature {
    private static final double MAX_USER_TEMPERATURE = 99.0;
    private static final double MIN_USER_TEMPERATURE = -99.0;

    @Column(name = "temperature")
    private Double temperature;

    public UserTemperature(double value) {
        validateUserTemperature(value);
        this.temperature = value;
    }

    private void validateUserTemperature(double temperature) {
        if (temperature > MAX_USER_TEMPERATURE) {
            throw new UserBadRequestException(ErrorCode.BAD_REQUEST,
                    MessageFormat.format("유저의 온도는 {0}도 이하입니다.", MAX_USER_TEMPERATURE));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTemperature that = (UserTemperature) o;
        return Objects.equals(temperature, that.temperature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature);
    }
}
