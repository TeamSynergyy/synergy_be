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
public class ProjectName {
    @Column(name = "name", nullable = false)
    private String name;

    public ProjectName(String value) {
        validateNotNull(value);
        this.name = value;
    }

    private void validateNotNull(String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new ProjectBadRequestException("프로젝트 제목은 필수 항목입니다.");
        }
    }
}
