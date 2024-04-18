package com.seoultech.synergybe.domain.schedule.vo;

import com.seoultech.synergybe.domain.schedule.exception.ScheduleBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class ScheduleTitle {
    private static final int MIN_TITLE_LENGTH = 1;
    private static final int MAX_TITLE_LENGTH = 30;


    @Column(name = "title", nullable = false)
    private String title;

    public ScheduleTitle(String value) {
        validateNotNull(value);
        this.title = value;
    }

    private void validateNotNull(String title) {
        if (title == null) {
            throw new ScheduleBadRequestException("일정 제목은 필수 항목입니다.");
        }
    }

    private void validateLength(String title) {
        if (MIN_TITLE_LENGTH > title.length() || title.length() > MAX_TITLE_LENGTH) {
            throw new ScheduleBadRequestException(
                    MessageFormat.format(
                            "일정 제목의 길이는 {0} 이상 {1} 이하여야 합니다.",
                            MIN_TITLE_LENGTH, MAX_TITLE_LENGTH
                    )
            );
        }
    }


}
