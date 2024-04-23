package com.seoultech.synergybe.domain.project.vo;

import com.seoultech.synergybe.domain.project.exception.ProjectBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectPeriod {

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    public ProjectPeriod(LocalDateTime startAt, LocalDateTime endAt, String leaderId) {
        validateNotNull(startAt, endAt, leaderId);
        this.startAt = startAt;
        this.endAt = endAt;
    }

    private void validateNotNull(LocalDateTime startAt, LocalDateTime endAt, String leaderId) {
        if (startAt == null || endAt == null || leaderId == null) {
            throw new ProjectBadRequestException("시작일자 종료일자 리더는 필수 항목 입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectPeriod that = (ProjectPeriod) o;
        return Objects.equals(startAt, that.startAt) && Objects.equals(endAt, that.endAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAt, endAt);
    }
}
