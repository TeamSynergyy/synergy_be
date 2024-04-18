package com.seoultech.synergybe.domain.schedule.vo;

import com.seoultech.synergybe.domain.schedule.exception.ScheduleBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchedulePeriod {

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    public SchedulePeriod(LocalDateTime startAt, LocalDateTime endAt) {
        validateStartAndEnd(startAt, endAt);
        this.startAt = startAt;
        this.endAt = endAt;
    }

    private void validateStartAndEnd(LocalDateTime startAt, LocalDateTime endAt) {
        if (endAt.isBefore(startAt)) {
            throw new ScheduleBadRequestException("종료시간은 시작시간 이전일 수 없습니다");
        }
    }
}
