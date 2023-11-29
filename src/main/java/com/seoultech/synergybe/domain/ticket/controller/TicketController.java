package com.seoultech.synergybe.domain.ticket.controller;

import com.seoultech.synergybe.domain.ticket.dto.TicketRequest;
import com.seoultech.synergybe.domain.ticket.dto.TicketResponse;
import com.seoultech.synergybe.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/ticket")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(request));

    }

}
