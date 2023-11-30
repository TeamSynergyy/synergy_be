package com.seoultech.synergybe.domain.ticket.service;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.ticket.Ticket;
import com.seoultech.synergybe.domain.ticket.TicketStatus;
import com.seoultech.synergybe.domain.ticket.dto.TicketRequest;
import com.seoultech.synergybe.domain.ticket.dto.TicketResponse;
import com.seoultech.synergybe.domain.ticket.repository.TicketRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.exception.InvalidAccessException;
import com.seoultech.synergybe.system.exception.NotExistTicketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ProjectService projectService;

    public TicketResponse createTicket(TicketRequest request, User user) {
        Project project = projectService.findProjectById(request.getProjectId());
        Ticket savedTicket = ticketRepository.save(request.toEntity(user, project));

        return TicketResponse.from(savedTicket);
    }

    public List<TicketResponse> getTicketList(TicketRequest request) {
        List<Ticket> tickets = ticketRepository.findAllByProjectId(request.getProjectId());

        return TicketResponse.from(tickets);
    }

    public TicketResponse updateTicket(TicketRequest request, User user, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(NotExistTicketException::new);

        if (!ticket.getUser().equals(user)) {
            throw new InvalidAccessException();
        }

        Ticket updatedTicket = ticket.update(request, checkStatus(request.getStatus()));

        return TicketResponse.from(updatedTicket);
    }

    private TicketStatus checkStatus(String status) {
        switch (status) {
            case "backlog":
                return TicketStatus.BACKLOG;
            case "in_progress":
                return TicketStatus.IN_PROGRESS;
            case "review":
                return TicketStatus.REVIEW;
            default:
                return TicketStatus.DONE;
        }
    }

    public TicketResponse deleteTicket(Long ticketId, User user) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(NotExistTicketException::new);

        if (!ticket.getUser().equals(user)) {
            throw new InvalidAccessException();
        }
        ticketRepository.delete(ticket);

        return TicketResponse.from(ticket);
    }
}
