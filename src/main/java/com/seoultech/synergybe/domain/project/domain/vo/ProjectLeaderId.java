package com.seoultech.synergybe.domain.project.domain.vo;

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
    private Long leaderId;

    public ProjectLeaderId(Long value) {
        validateNotNull(value);
        this.leaderId = value;
    }

    private void validateNotNull(Long leaderId) {
        if (Objects.isNull(leaderId)) {
            throw new ProjectBadRequestException("리더 ID는 필수 항목입니다.");
        }
    }
}
