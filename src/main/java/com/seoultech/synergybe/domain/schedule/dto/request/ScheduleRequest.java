package com.seoultech.synergybe.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ScheduleRequest {
    private String title;
    private String content;
    private String label;
    private Long projectId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    public Schedule toEntity(Project project) {
        return Schedule.builder()
                .title(title)
                .content(content)
                .label(label)
                .project(project)
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }
}
