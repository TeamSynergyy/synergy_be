package com.seoultech.synergybe.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateScheduleRequest(
        @NotBlank(message = "제목은 필수항목입니다.")
        String title,
        @NotBlank(message = "제목은 필수항목입니다.")
        String content,
        @NotBlank(message = "제목은 필수항목입니다.")
        String label,
        @NotBlank(message = "제목은 필수항목입니다.")
        String projectId,
        @NotBlank(message = "시작일은 필수항목입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
        LocalDateTime startAt,
        @NotBlank(message = "종료일은 필수항목입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
        LocalDateTime endAt
) {

//    public Schedule toEntity(Project project) {
//        return Schedule.builder()
//                .title(title)
//                .content(content)
//                .label(label)
//                .project(project)
//                .startAt(startAt)
//                .endAt(endAt)
//                .build();
//    }
}
