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
     * 기존 orderNum 보다 클 경우 사잇값 -1
     * 기존 orderNum 보다 작을 경우 사잇값 +1
     *
     * case 2 다른 Status일 경우
     * 이전 status의 ticket들의 orderNum이 큰 ticket에 대해 -1
     * 수정 할 status의 ticket들 중 orderNum이 큰 ticket들에 대해 +1
     */
    public List<TicketResponse> changeTickets(TicketRequest request, User user, Long ticketId) {
        // check User
        List<User> authUsers = projectService.getUserListByProject(request.getProjectId());
        checkUser(authUsers, user);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(NotExistTicketException::new);

        boolean isEqualStatus = false;

        // 동일 status 인지 check
        TicketStatus preStatus = ticket.getStatus();    // 이전 ticket의 상태
        TicketStatus postStatus = checkStatus(request.getStatus()); // 이후 ticket의 상태

        if (preStatus.equals(postStatus)) {
            isEqualStatus = true;
        }

        Integer preTicketOrderNum = ticket.getOrderNumber();    // 이전 ticket의 index 번호
        Integer postTicketOrderNum = request.getOrderNumber();  // 이후 ticket의 index 번호

        // assignedUser 추가
        if (!request.getAssignedUserIds().isEmpty()) {
            // 기존 assignedUser을 삭제 후 추가해야함
            ticket.deleteAssignedUsers();
            ticketUserService.deleteAssignedUser(ticket);
            List<User> assignedUsers = userService.getUsers(request.getAssignedUserIds());
            for (User assignedUser : assignedUsers) {
                ticketUserService.createTicketUser(ticket, assignedUser);
            }
        }

        // status가 동일한지 check
        if (isEqualStatus) {
            return equalStatus(preTicketOrderNum, postTicketOrderNum, ticket, request);
        } else {
            return notEqualStatus(preTicketOrderNum, postTicketOrderNum, ticket, request, preStatus, postStatus);
        }
    }

    private List<TicketResponse> equalStatus(int preTicketOrderNum, int postTicketOrderNum, Ticket ticket, TicketRequest request) {
        List<Ticket> changeTicketList = new ArrayList<>();
        if (postTicketOrderNum > preTicketOrderNum) {
            List<Ticket> tickets = ticketRepository.findAllLowToBigOrderNumber(request.getProjectId(), request.getStatus(), preTicketOrderNum, postTicketOrderNum);
            decreaseOrderNum(tickets);
            Ticket updatedTicket = ticket.update(request, checkStatus(request.getStatus()));
            changeTicketList.addAll(tickets);
            changeTicketList.add(updatedTicket);
        } else if (preTicketOrderNum > postTicketOrderNum) {
            List<Ticket> tickets = ticketRepository.findAllBigToLowOrderNumber(request.getProjectId(), request.getStatus(), postTicketOrderNum, preTicketOrderNum);
            increaseOrderNum(tickets);
            Ticket updatedTicket = ticket.update(request, checkStatus(request.getStatus()));
            changeTicketList.addAll(tickets);
            changeTicketList.add(updatedTicket);
        }
        return TicketResponse.from(changeTicketList);
    }

    private List<TicketResponse> notEqualStatus(int preTicketOrderNum, int postTicketOrderNum, Ticket ticket, TicketRequest request,
                                                TicketStatus preStatus, TicketStatus postStatus) {
        // status가 다를 경우
        // 이전 ticket들을 가져옴, orderNum이 pre 보다 큰
        List<Ticket> changeTicketList = new ArrayList<>();

        // 기존 status의 tickets
        List<Ticket> preStatusTickets = ticketRepository.findAllByBiggerOrderNumber(request.getProjectId(), preStatus.name(), preTicketOrderNum);

        // 요청된 ticket의 orderNum 보다 큰 orderNum을 가진 ticket들에 대해 -1
        decreaseOrderNum(preStatusTickets);
        changeTicketList.addAll(preStatusTickets);


        // 요청된 status의 tickets
        List<Ticket> postStatusTickets = ticketRepository.findAllByBiggerOrderNumber(request.getProjectId(), postStatus.name(), postTicketOrderNum);

        // 요청된 ticket의 orderNum 보다 작은 orderNum을 가진 ticket들에 대해 +1
        increaseOrderNum(postStatusTickets);
        changeTicketList.addAll(postStatusTickets);
        Ticket updatedTicket = ticket.update(request, checkStatus(request.getStatus()));
        changeTicketList.add(updatedTicket);

        return TicketResponse.from(changeTicketList);
    }

    private void increaseOrderNum(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            ticket.increaseOrderNum();
        }
    }

    private void decreaseOrderNum(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            ticket.decreaseOrderNum();
        }
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

    public TicketResponse updateTicket(TicketRequest request, User user, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(NotExistTicketException::new);
        // check User
        List<User> authUsers = projectService.getUserListByProject(ticket.getProject().getId());
        checkUser(authUsers, user);

        // assignedUser 수정
        if (!request.getAssignedUserIds().isEmpty()) {
            // 기존 assignedUser을 삭제 후 추가해야함
            ticket.deleteAssignedUsers();
            ticketUserService.deleteAssignedUser(ticket);

            List<User> assignedUsers = userService.getUsers(request.getAssignedUserIds());
            for (User assignedUser : assignedUsers) {
                ticketUserService.createTicketUser(ticket, assignedUser);
            }
        }

        ticket.update(request, checkStatus(request.getStatus()));

        return TicketResponse.from(ticket);
    }
}
