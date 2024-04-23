package com.seoultech.synergybe.domain.user.vo;

import com.seoultech.synergybe.domain.user.exception.UserBadRequestException;
import com.seoultech.synergybe.system.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEmail {
    @Column(name = "email")
    private String email;

    public UserEmail(String value) {
        validateUserEmail(value);
        this.email = value;
    }

    private void validateUserEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "이메일은 필수 항목입니다.");
        }
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        Pattern emailPattern = Pattern.compile(pattern);
        Matcher matcher = emailPattern.matcher(email);

        if (!matcher.matches()) {
            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "이메일 형식에 맞지 않습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEmail userEmail = (UserEmail) o;
        return Objects.equals(email, userEmail.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
