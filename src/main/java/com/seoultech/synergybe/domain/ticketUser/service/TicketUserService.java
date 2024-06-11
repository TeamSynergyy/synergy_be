package com.seoultech.synergybe.domain.ticketUser.service;

import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.generator.TokenGenerator;
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
    private final TokenGenerator tokenGenerator;

    public void createTicketUser(Ticket ticket, User user) {
        Optional<TicketUser> ticketUserOptional = ticketUserRepository.findByTicketIdAndUserId(ticket.getTicketToken(), user.getUserToken());

        if (ticketUserOptional.isPresent()) {
            // 이미 생성됨
        } else {
            Long ticketUserId = idGenerator.generateId();
            String ticketUserToken = tokenGenerator.generateToken(IdPrefix.TICKET_USER);

            TicketUser ticketUser = TicketUser.builder()
                    .id(ticketUserId).ticketUserToken(ticketUserToken).ticket(ticket).user(user)
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
