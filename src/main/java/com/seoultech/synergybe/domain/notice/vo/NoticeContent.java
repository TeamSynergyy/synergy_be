package com.seoultech.synergybe.domain.notice.vo;

import com.seoultech.synergybe.domain.notice.exception.NoticeBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeContent {
    @Column(name = "content")
    private String content;

    public NoticeContent(String value) {
        validateNotNull(value);
        this.content = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoticeContent that = (NoticeContent) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    private void validateNotNull(String content) {
        if (content == null || content.isBlank()) {
            throw new NoticeBadRequestException("공지 내용은 필수 항목입니다.");
        }
    }
}
