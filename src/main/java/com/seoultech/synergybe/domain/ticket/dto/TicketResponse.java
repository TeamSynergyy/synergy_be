package com.seoultech.synergybe.domain.ticket.dto;

import com.seoultech.synergybe.domain.ticket.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
public class TicketResponse {
    private Long ticketId;
    private String title;
    private String tag;

    public static TicketResponse from(Ticket savedTicket) {
        return new TicketResponse(savedTicket.getId(), savedTicket.getTitle(),
                savedTicket.getTag());
    }

    public static List<TicketResponse> from(List<Ticket> tickets) {
        return tickets.stream()
                .map(ticket -> TicketResponse.builder()
                        .ticketId(ticket.getId())
                        .title(ticket.getTitle())
                        .tag(ticket.getTag())
                        .build())
                .collect(Collectors.toList());
    }
}
