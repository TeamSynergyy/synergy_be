package com.seoultech.synergybe.domain.project.vo;

import com.seoultech.synergybe.domain.project.exception.ProjectBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectLocation {

    @Column(name = "location")
    private Point location;

    public ProjectLocation(Point value) {
        validateNotNull(value);
        this.location = value;
    }

    private void validateNotNull(Point value) {
        if (value == null) {
            throw new ProjectBadRequestException("위치 정보는 필수 항목입니다.");
        }
    }

    public void updateLocation(Point value) {
        validateNotNull(value);
        this.location = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectLocation that = (ProjectLocation) o;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }
}
