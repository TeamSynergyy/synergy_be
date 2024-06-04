package com.seoultech.synergybe.domain.ticket.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public record CreateTicketRequest(
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        String projectId,
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        String title,
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        String content,
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        String tag,
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        String tagColor,
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        Integer orderNumber,
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        String status,
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        List<String> assignedUserIds,
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        Double assignedTime,
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
        LocalDateTime endAt
) {

//    public Ticket toEntity(Project project, TicketStatus status, Integer orderNumber) {
//        return Ticket.builder()
//                .title(title)
//                .content(content)
//                .tag(tag)
//                .orderNumber(orderNumber)
//                .endAt(endAt)
//                .status(status)
//                .assignedTime(assignedTime)
//                .tagColor(tagColor)
//                .project(project)
//                .build();
//    }
}
