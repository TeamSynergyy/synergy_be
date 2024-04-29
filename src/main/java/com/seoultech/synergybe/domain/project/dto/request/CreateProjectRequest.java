package com.seoultech.synergybe.domain.project.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.ProjectField;
import com.seoultech.synergybe.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.data.geo.Point;

import java.time.LocalDateTime;


public record CreateProjectRequest(
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

//    public Project toEntity(User user) {
//        Point point = new Point(longitude, latitude);
//        try {
//            return Project.builder()
//                    .name(name)
//                    .content(content)
//                    .field(field)
//                    .location(point)
//                    .startAt(startAt)
//                    .endAt(endAt)
//                    .leaderId(user.getUserId())
//                    .build();
//        } catch (Exception e) {
//            throw new IllegalArgumentException("point parse exception");
//        }
//    }
}
