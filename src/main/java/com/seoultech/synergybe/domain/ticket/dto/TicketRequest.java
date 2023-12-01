package com.seoultech.synergybe.domain.ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.ticket.Ticket;
import com.seoultech.synergybe.domain.ticket.TicketStatus;
import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class TicketRequest {
    private Long projectId;
    private String title;
    private String tag;
    private String tagColor;
    private Integer orderNumber;
    private String status;
    private List<String> assignedUserIds;
    private Double assignedTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    public Ticket toEntity(Project project, TicketStatus status, Integer orderNumber) {
        return Ticket.builder()
                .title(title)
                .tag(tag)
                .orderNumber(orderNumber)
                .endAt(endAt)
                .status(status)
                .assignedTime(assignedTime)
                .tagColor(tagColor)
                .project(project)
                .build();
    }
}
