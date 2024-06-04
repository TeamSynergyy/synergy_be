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
public class ProjectContent {
    @Column(name = "content", nullable = false)
    private String content;

    public ProjectContent(String value) {
        validateNotNull(value);
        this.content = value;
    }

    private void validateNotNull(String content) {
        if (content == null || content.isBlank()) {
            throw new ProjectBadRequestException("프로젝트 내용은 필수 항목입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectContent that = (ProjectContent) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
