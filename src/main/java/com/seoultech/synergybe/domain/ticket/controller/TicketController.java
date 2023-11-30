package com.seoultech.synergybe.domain.ticket.controller;

import com.seoultech.synergybe.domain.ticket.dto.TicketRequest;
import com.seoultech.synergybe.domain.ticket.dto.TicketResponse;
import com.seoultech.synergybe.domain.ticket.service.TicketService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/ticket")
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest request, @LoginUser String userId) {
        User user = userService.findUserById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(request, user));
    }

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<List<TicketResponse>> getTicketList(@RequestBody TicketRequest request) {

        return ResponseEntity.ok(ticketService.getTicketList(request));
    }

    @PutMapping(value = "/{ticketId}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable("ticketId") Long ticketId, @LoginUser String userId,
                                                       @RequestBody TicketRequest request) {
        User user = userService.findUserById(userId);

        return ResponseEntity.ok(ticketService.updateTicket(request, user));
    }

    @DeleteMapping(value = "/{ticketId}")
    public ResponseEntity<TicketResponse> deleteTicket(@PathVariable("ticketId") Long ticketId, @LoginUser String userId) {
        User user = userService.findUserById(userId);

        return ResponseEntity.ok(ticketService.deleteTicket(ticketId, user));
    }
}
