package com.seoultech.synergybe.domain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Embeddable
public class UserEmail {
    @Column(name = "email")
    private String email;

    protected UserEmail() {

    }

    public UserEmail(String value) {
        validateUserEmail(value);
        this.email = value;
    }

    private void validateUserEmail(String email) {
        if (email == null || email.isBlank()) {
//            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "이메일은 필수 항목입니다.");
        }
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        Pattern emailPattern = Pattern.compile(pattern);
        Matcher matcher = emailPattern.matcher(email);

        if (!matcher.matches()) {
//            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "이메일 형식에 맞지 않습니다.");
        }
    }
}
