package com.seoultech.synergybe.domain.ticket.dto.response;

import com.seoultech.synergybe.domain.ticket.Ticket;
import com.seoultech.synergybe.domain.ticket.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record GetTicketResponse(
        String ticketId,
        String projectId,
        int orderNumber,
        String title,
        String tag,
        String tagColor,
        TicketStatus status,
        List<String> assignedUserIds,
        Double assignedTime,
        LocalDateTime endAt
) {

//    public static GetTicketResponse from(Ticket ticket) {
//        return GetTicketResponse.builder()
//                .ticketId(ticket.getId())
//                .projectId(ticket.getProject().getId())
//                .orderNumber(ticket.getOrderNumber())
//                .title(ticket.getTitle())
//                .tag(ticket.getTag())
//                .tagColor(ticket.getTagColor())
//                .status(ticket.getStatus())
//                .assignedUserIds(ticket.getTicketUsers().stream().map(ticketUser -> ticketUser.getUser().getUserId()).collect(Collectors.toList()))
//                .assignedTime(ticket.getAssignedTime())
//                .endAt(ticket.getEndAt())
//                .build();
//    }
//
//    public static List<GetTicketResponse> from(List<Ticket> tickets) {
//        return tickets.stream()
//                .map(ticket -> GetTicketResponse.builder()
//                        .ticketId(ticket.getId())
//                        .projectId(ticket.getProject().getId())
//                        .orderNumber(ticket.getOrderNumber())
//                        .title(ticket.getTitle())
//                        .tag(ticket.getTag())
//                        .tagColor(ticket.getTagColor())
//                        .status(ticket.getStatus())
//                        .assignedUserIds(ticket.getTicketUsers().stream().map(ticketUser -> ticketUser.getUser().getUserId()).collect(Collectors.toList()))
//                        .assignedTime(ticket.getAssignedTime())
//                        .endAt(ticket.getEndAt())
//                        .build())
//                .collect(Collectors.toList());
//    }
}
