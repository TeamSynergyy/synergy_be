package com.seoultech.synergybe.domain.project.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.ProjectField;
import com.seoultech.synergybe.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreateProjectRequest {
    private String name;

    private String content;

    private ProjectField field;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    public Project toEntity(User user) {
        return Project.builder()
                .name(name)
                .content(content)
                .field(field)
                .startAt(startAt)
                .endAt(endAt)
                .leaderId(user.getUserId())
                .build();
    }
}
