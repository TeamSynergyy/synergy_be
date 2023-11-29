package com.seoultech.synergybe.domain.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TicketResponse {
    private Long ticketId;
    private String title;
    private String tag;
}
