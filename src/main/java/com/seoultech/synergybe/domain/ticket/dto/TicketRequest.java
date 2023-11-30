package com.seoultech.synergybe.domain.ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.ticket.Ticket;
import com.seoultech.synergybe.domain.ticket.TicketStatus;
import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class TicketRequest {
    private Long projectId;
    private Long ticketId;
    private String title;
    private String tag;
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    public Ticket toEntity(User user, Project project) {
        return Ticket.builder()
                .title(title)
                .tag(tag)
                .user(user)
                .project(project)
                .build();
    }
}
