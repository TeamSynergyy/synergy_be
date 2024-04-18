package com.seoultech.synergybe.domain.schedule.vo;

import com.seoultech.synergybe.domain.schedule.exception.ScheduleBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleContent {
    private static final int MAX_LENGTH_CONTENT = 100;
    @Column(name = "content", nullable = false)
    private String content;

    public ScheduleContent(String value) {
        validateLength(value);
        this.content = value;
    }

    private void validateLength(String content) {
        if (content.length() > MAX_LENGTH_CONTENT) {
            throw new ScheduleBadRequestException("일정 내용은 100자 이하로 작성해야합니다.");
        }
    }
}
