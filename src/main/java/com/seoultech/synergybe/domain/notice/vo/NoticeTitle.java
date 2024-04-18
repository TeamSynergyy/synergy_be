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

    private void validateNotNull(String title) {
        if (Objects.isNull(title) || title.isBlank()) {
            throw new NoticeBadRequestException("공지사항 제목은 필수 항목입니다.");
        }
    }
}
