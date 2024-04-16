package com.seoultech.synergybe.domain.rate.vo;

import com.seoultech.synergybe.domain.rate.exception.RateBadRequestException;
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
public class RateContent {
    private static final int MAX_CONTENT_LENGTH = 100;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    public RateContent(String value) {
        validateNotNull(value);
        validateRateContentLength(value);
        this.content = value;
    }

    private void validateNotNull(String content) {
        if (Objects.isNull(content) || content.isBlank()) {
            throw new RateBadRequestException("후기는 필수 항목입니다.");
        }
    }

    private void validateRateContentLength(String content) {
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new RateBadRequestException(MessageFormat.format(
                    "내용은 {0} 자 이하여야 합니다.", MAX_CONTENT_LENGTH
            ));
        }
    }
}
