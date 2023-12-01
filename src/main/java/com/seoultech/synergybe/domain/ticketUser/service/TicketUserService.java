package com.seoultech.synergybe.domain.ticketUser.service;

import com.seoultech.synergybe.domain.ticket.Ticket;
import com.seoultech.synergybe.domain.ticketUser.TicketUser;
import com.seoultech.synergybe.domain.ticketUser.repository.TicketUserRepository;
import com.seoultech.synergybe.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketUserService {
    private final TicketUserRepository ticketUserRepository;

    public void createTicketUser(Ticket ticket, User user) {
        Optional<TicketUser> ticketUserOptional = ticketUserRepository.findByTicketIdAndUserUserId(ticket.getId(), user.getUserId());

        if (ticketUserOptional.isPresent()) {
            // 이미 생성됨
        } else {
            TicketUser ticketUser = new TicketUser(ticket, user);
            ticket.getTicketUsers().add(ticketUser);
            ticketUserRepository.save(ticketUser);
        }
    }

    public List<String> getTicketUserIds(Long ticketId) {
        return ticketUserRepository.findTicketUserIdsByTicketId(ticketId);
    }
}
