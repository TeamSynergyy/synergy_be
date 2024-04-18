package com.seoultech.synergybe.domain.post.vo;

import com.seoultech.synergybe.domain.post.exception.PostBadRequestException;
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
public class PostTitle {
    private static final int MAX_POST_TITLE_LENGTH = 20;

    @Column(name = "title")
    private String title;

    public PostTitle(String value) {
        validateNotNull(value);
        validatePostTitleLength(value);
        this.title = value;
    }

    private void validateNotNull(String title) {
        if (Objects.isNull(title) || title.isBlank()) {
            throw new PostBadRequestException("제목은 필수 항목입니다.");
        }
    }

    private void validatePostTitleLength(String title) {
        if (title.length() > MAX_POST_TITLE_LENGTH) {
            throw new PostBadRequestException(MessageFormat.format("제목의 길이는 {0} 자 이내여야 합니다.",
                            MAX_POST_TITLE_LENGTH));
        }
    }
}
