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
public class NoticeTitle {
    @Column(name = "title", nullable = false)
    private String title;

    public NoticeTitle(String value) {
        validateNotNull(value);
        this.title = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoticeTitle that = (NoticeTitle) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    private void validateNotNull(String title) {
        if (title == null || title.isBlank()) {
            throw new NoticeBadRequestException("공지사항 제목은 필수 항목입니다.");
        }
    }
}
