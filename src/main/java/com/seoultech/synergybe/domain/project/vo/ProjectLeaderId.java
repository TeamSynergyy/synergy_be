package com.seoultech.synergybe.domain.project.vo;

import com.seoultech.synergybe.domain.project.exception.ProjectBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectLeaderId {
    @Column(name = "leader_id")
    private String leaderId;

    public ProjectLeaderId(String value) {
        validateNotNull(value);
        this.leaderId = value;
    }

    private void validateNotNull(String leaderId) {
        if (Objects.isNull(leaderId) || leaderId.isBlank()) {
            throw new ProjectBadRequestException("리더 ID는 필수 항목입니다.");
        }
    }
}
