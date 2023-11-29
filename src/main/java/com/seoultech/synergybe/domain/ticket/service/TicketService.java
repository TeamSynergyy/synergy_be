package com.seoultech.synergybe.domain.ticket.service;

import com.seoultech.synergybe.domain.ticket.dto.TicketRequest;
import com.seoultech.synergybe.domain.ticket.dto.TicketResponse;
import com.seoultech.synergybe.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketResponse createTicket(TicketRequest request) {

    }
}
