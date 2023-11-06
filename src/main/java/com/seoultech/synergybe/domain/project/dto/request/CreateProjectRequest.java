package com.seoultech.synergybe.domain.project.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.ProjectField;
import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreateProjectRequest {
    private String name;

    private String content;

    private ProjectField field;

    private Double longitude;

    private Double latitude;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    public Project toEntity(User user) {
        Point point = new Point(longitude, latitude);
        try {
            return Project.builder()
                    .name(name)
                    .content(content)
                    .field(field)
                    .location(point)
                    .startAt(startAt)
                    .endAt(endAt)
                    .leaderId(user.getUserId())
                    .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("point parse exception");
        }
    }
}
