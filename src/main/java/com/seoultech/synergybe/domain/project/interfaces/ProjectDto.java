package com.seoultech.synergybe.domain.project.interfaces;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seoultech.synergybe.domain.common.PageInfo;
import com.seoultech.synergybe.domain.project.domain.ProjectCommand;
import com.seoultech.synergybe.domain.project.domain.ProjectField;
import com.seoultech.synergybe.domain.project.interfaces.dto.response.GetProjectResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectDto {

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

    public record UpdateProjectRequest(
            @NotBlank(message = "프로젝트 Token은 필수항목입니다.")
            String projectToken,
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
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
            LocalDateTime startAt,
            @NotBlank(message = "종료일시는 필수항목입니다.")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
            LocalDateTime endAt
    ) {
    }

    public record RegisterResponse(
            String projectToken
    ) {

    }

    public record GetListProjectInfo(
            List<GetProjectResponse> getProjectResponses,
            PageInfo pageInfo
    ) {
    }

    public record Main (
            String projectId,
            String name,
            String content,
            String field,
            String status,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String leaderId,

            Point location,
            List<String> teamUserIds
    ) {
    }

}
