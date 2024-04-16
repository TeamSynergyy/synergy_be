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
public class ProjectInformation {

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "leader_id")
    private String leaderId;

    public ProjectInformation(LocalDateTime startAt, LocalDateTime endAt, String leaderId) {
        validateNotNull(startAt, endAt, leaderId);
        this.startAt = startAt;
        this.endAt = endAt;
        this.leaderId = leaderId;
    }

    private void validateNotNull(LocalDateTime startAt, LocalDateTime endAt, String leaderId) {
        if (Objects.isNull(startAt) || Objects.isNull(endAt) || Objects.isNull(leaderId)) {
            throw new ProjectBadRequestException("시작일자 종료일자 리더는 필수 항목 입니다.");
        }
    }
}
