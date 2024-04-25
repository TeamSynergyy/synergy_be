package com.seoultech.synergybe.domain.ticketUser.service;

import com.seoultech.synergybe.domain.common.idgenerator.IdGenerator;
import com.seoultech.synergybe.domain.common.idgenerator.IdPrefix;
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
    private final IdGenerator idGenerator;

    public void createTicketUser(Ticket ticket, User user) {
        Optional<TicketUser> ticketUserOptional = ticketUserRepository.findByTicketIdAndUserId(ticket.getId(), user.getId());

        if (ticketUserOptional.isPresent()) {
            // 이미 생성됨
        } else {
            String ticketUserId = idGenerator.generateId(IdPrefix.TICKET_USER);
            TicketUser ticketUser = TicketUser.builder()
                    .id(ticketUserId).ticket(ticket).user(user)
                    .build();
            ticket.getTicketUsers().add(ticketUser);
            ticketUserRepository.save(ticketUser);
        }
    }

    public List<String> getTicketUserIds(String ticketId) {
        return ticketUserRepository.findTicketUserIdsByTicketId(ticketId);
    }

    public void deleteAssignedUser(Ticket ticket) {
        ticketUserRepository.deleteAllByTicket(ticket);
    }
}
