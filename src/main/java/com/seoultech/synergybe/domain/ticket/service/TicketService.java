package com.seoultech.synergybe.domain.ticket.service;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.ticket.Ticket;
import com.seoultech.synergybe.domain.ticket.TicketStatus;
import com.seoultech.synergybe.domain.ticket.dto.TicketRequest;
import com.seoultech.synergybe.domain.ticket.dto.TicketResponse;
import com.seoultech.synergybe.domain.ticket.repository.TicketRepository;
import com.seoultech.synergybe.domain.ticketUser.service.TicketUserService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.exception.InvalidAccessException;
import com.seoultech.synergybe.system.exception.NotExistTicketException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketUserService ticketUserService;
    private final ProjectService projectService;
    private final UserService userService;

    /**
     * todo
     * 생성시 orderNumber은 가장 마지막으로 배정
     * @param request
     * @return
     */
    public TicketResponse createTicket(TicketRequest request, User allocatedUser) {
        // check User
        List<User> authUsers = projectService.getUserListByProject(request.getProjectId());

        checkUser(authUsers, allocatedUser);

        Project project = projectService.findProjectById(request.getProjectId());
        Integer lastOrderNum = ticketRepository.findLastOrderNumber(request.getStatus(), request.getProjectId());
        Ticket savedTicket = ticketRepository.save(request.toEntity(project, checkStatus(request.getStatus()), lastOrderNum));

        if (!request.getAssignedUserIds().isEmpty()) {
            // assignedUser 추가
            List<User> assignedUsers = userService.getUsers(request.getAssignedUserIds());
            for (User assignedUser : assignedUsers) {
                ticketUserService.createTicketUser(savedTicket, assignedUser);
            }
        }

        return TicketResponse.from(savedTicket);
    }

    public List<TicketResponse> getTicketList(Long projectId) {
        List<Ticket> tickets = ticketRepository.findAllByProjectId(projectId);

        return TicketResponse.from(tickets);
    }

    /** todo
     * summary
     * 1. 업데이트할 ticket 정보의 요청을 받아온다.
     * 2. 이 요청으로 인하여 정보가 변해야하는 다른 티켓들의 정보들을 탐색한다.
     * 3. 변경이 필요한 티켓들에 대해 변경을 반영한다.
     *
     * case 1 동일한 Status일 경우
     * 요청받은 order 보다 뒤에 존재하는 order가 있을 경우 모두 +1일 해준뒤 업데이트를 한다
     *
     * case 2 다른 Status일 경우
     * 1. status를 변경한다
     * 2. 요청받은 order 보다 뒤에 존재하는 order가 있을 경우 해당되는 티켓들을 모두 +1일 해준 뒤 업데이트를 한다
     * 3. 변경된 티겟들만 정보를 반환한다.
     */
    public List<TicketResponse> updateTickets(TicketRequest request, User user, Long ticketId) {
        // check User
        List<User> authUsers = projectService.getUserListByProject(request.getProjectId());
        checkUser(authUsers, user);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(NotExistTicketException::new);

        // get tickets
        List<Ticket> tickets = ticketRepository.findAllByProjectIdAndStatusAndOrder(request.getProjectId(), request.getStatus(), request.getOrderNumber());

        if (request.getAssignedUserIds() != null) {
            // assignedUser 추가
            List<User> assignedUsers = userService.getUsers(request.getAssignedUserIds());
            for (User assignedUser : assignedUsers) {
                ticketUserService.createTicketUser(ticket, assignedUser);
            }
        }


        // 추가로 변경될 tickets 들이 있는지 check
        // 변경될 tickets들이 없을 경우
        if (tickets.isEmpty()) {
            List<Ticket> changeTicketList = new ArrayList<>();
            Ticket updatedTicket = ticket.update(request, checkStatus(request.getStatus()));
            changeTicketList.add(updatedTicket);
            return TicketResponse.from(changeTicketList);
        }

        // 변경될 tickets 들이 있을 경우
        // 해당하는 ticket들에 대해 +1을 해준다

        List<Ticket> changeTicketList = new ArrayList<>();
        for (Ticket changeTicket : tickets) {
            changeTicketList.add(changeTicket.increaseOrderNum());
        }
        Ticket updatedTicket = ticket.update(request, checkStatus(request.getStatus()));
        changeTicketList.add(updatedTicket);

        // 변경된 ticket들을 반환한다

        return TicketResponse.from(changeTicketList);
    }

    // check user authentication
    private void checkUser(List<User> authUsers, User user) {
        boolean userFound = false;
        for (User authUser : authUsers) {
            if (authUser.getUserId().equals(user.getUserId())) {
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            throw new InvalidAccessException();
        }
    }

    private TicketStatus checkStatus(String status) {
        switch (status) {
            case "BACKLOG":
                return TicketStatus.BACKLOG;
            case "IN_PROGRESS":
                return TicketStatus.IN_PROGRESS;
            case "REVIEW":
                return TicketStatus.REVIEW;
            default:
                return TicketStatus.DONE;
        }
    }

    public TicketResponse deleteTicket(Long ticketId, User user) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(NotExistTicketException::new);

        // check User
        List<User> authUsers = projectService.getUserListByProject(ticket.getProject().getId());
        checkUser(authUsers, user);

        ticketRepository.delete(ticket);

        return TicketResponse.from(ticket);
    }
}
