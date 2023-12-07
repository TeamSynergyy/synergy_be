package com.seoultech.synergybe.domain.ticket.dto;

import com.seoultech.synergybe.domain.ticket.Ticket;
import com.seoultech.synergybe.domain.ticket.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
public class TicketResponse {
    private Long ticketId;
    private Long projectId;
    private Integer orderNumber;
    private String title;
    private String tag;
    private String tagColor;
    private TicketStatus status;
    private List<String> assignedUserIds;
    private Double assignedTime;
    private LocalDateTime endAt;

    public static TicketResponse from(Ticket ticket) {
        return TicketResponse.builder()
                .ticketId(ticket.getId())
                .projectId(ticket.getProject().getId())
                .orderNumber(ticket.getOrderNumber())
                .title(ticket.getTitle())
                .tag(ticket.getTag())
                .tagColor(ticket.getTagColor())
                .status(ticket.getStatus())
                .assignedUserIds(ticket.getTicketUsers().stream().map(ticketUser -> ticketUser.getUser().getUserId()).collect(Collectors.toList()))
                .assignedTime(ticket.getAssignedTime())
                .endAt(ticket.getEndAt())
                .build();
    }

    public static List<TicketResponse> from(List<Ticket> tickets) {
        return tickets.stream()
                .map(ticket -> TicketResponse.builder()
                        .ticketId(ticket.getId())
                        .projectId(ticket.getProject().getId())
                        .orderNumber(ticket.getOrderNumber())
                        .title(ticket.getTitle())
                        .tag(ticket.getTag())
                        .tagColor(ticket.getTagColor())
                        .status(ticket.getStatus())
                        .assignedUserIds(ticket.getTicketUsers().stream().map(ticketUser -> ticketUser.getUser().getUserId()).collect(Collectors.toList()))
                        .assignedTime(ticket.getAssignedTime())
                        .endAt(ticket.getEndAt())
                        .build())
                .collect(Collectors.toList());
    }
}
