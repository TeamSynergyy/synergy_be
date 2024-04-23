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
public class PostContent {
    private static final int MAX_CONTENT_LENGTH = 500;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    public PostContent(String value) {
        validateNotNull(value);
        validatePostContentLength(value);
        this.content = value;
    }

    private void validateNotNull(String content) {
        if (content == null || content.isBlank()) {
            throw new PostBadRequestException("내용은 필수 항목입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostContent that = (PostContent) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    private void validatePostContentLength(String content) {
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new PostBadRequestException(MessageFormat.format("내용의 길이는 {0} 자 이내여야 합니다.", MAX_CONTENT_LENGTH));
        }
    }
}
