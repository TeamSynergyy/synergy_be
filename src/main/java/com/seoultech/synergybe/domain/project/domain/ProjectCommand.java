package com.seoultech.synergybe.domain.project.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class ProjectCommand {
    public record RegisterProjectRequest(
            @NotBlank(message = "이름은 필수항목입니다.")
            String name,
            @NotBlank(message = "내용은 필수항목입니다.")
            String content,
            @NotBlank(message = "분야는 필수항목입니다.")
            ProjectField field,
            @NotBlank(message = "경도은 필수항목입니다.")
            Double longitude,
            @NotBlank(message = "위도는 필수항목입니다.")
            Double latitude,
            @NotBlank(message = "시작일시는 필수항목입니다.")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
            LocalDateTime startAt,
            @NotBlank(message = "종료일시는 필수항목입니다.")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
            LocalDateTime endAt

    ) {
    }
}
