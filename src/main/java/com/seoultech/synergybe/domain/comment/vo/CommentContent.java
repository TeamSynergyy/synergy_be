package com.seoultech.synergybe.domain.comment.vo;

import com.seoultech.synergybe.domain.comment.exception.CommentBadRequestException;
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
public class CommentContent {
    private static final int MIN_CONTENT_LENGTH = 1;
    private static final int MAX_CONTENT_LENGTH = 50;

    @Column(name = "content", nullable = false)
    private String content;

    public CommentContent(String value) {
        validateLength(value);
        this.content = value;
    }

    private void validateLength(String value) {
        if (MIN_CONTENT_LENGTH > value.length() || value.length() > MAX_CONTENT_LENGTH) {
            throw new CommentBadRequestException(
                    MessageFormat.format(
                            "댓글은 {0} 자 이상 {1}자 이하로 작성해야합니다.",
                            MIN_CONTENT_LENGTH, MAX_CONTENT_LENGTH
                    )
            );
        }
    }

    public CommentContent updateContent(String value) {
        return value != null ? new CommentContent(value) : this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentContent that = (CommentContent) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
