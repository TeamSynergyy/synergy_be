package com.seoultech.synergybe.domain.comment.vo;

import com.seoultech.synergybe.domain.comment.exception.CommentBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentContent {
    private static final int CONTENT_MIN_LENGTH = 10;
    private static final int CONTENT_MAX_LENGTH = 200;

    @Column(name = "content")
    private String content;

    public CommentContent(String value) {
        validateLength(value);
        this.content = value;
    }

    private void validateLength(String value) {
        if (CONTENT_MIN_LENGTH > value.length() || value.length() > CONTENT_MAX_LENGTH) {
            throw new CommentBadRequestException(
                    MessageFormat.format(
                            "댓글은 {0} 자 이상 {1}자 이하로 작성해야합니다.",
                            CONTENT_MIN_LENGTH, CONTENT_MAX_LENGTH
                    )
            );
        }
    }

    public CommentContent updateContent(String value) {
        return value != null ? new CommentContent(value) : this;
    }
}
