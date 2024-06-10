package com.seoultech.synergybe.domain.ticket.service;

import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.ticket.Ticket;
import com.seoultech.synergybe.domain.ticket.TicketStatus;
import com.seoultech.synergybe.domain.ticket.dto.request.CreateTicketRequest;
import com.seoultech.synergybe.domain.ticket.dto.response.GetTicketResponse;
import com.seoultech.synergybe.domain.ticket.exception.TicketNotFoundException;
import com.seoultech.synergybe.domain.ticket.repository.TicketRepository;
import com.seoultech.synergybe.domain.ticketUser.service.TicketUserService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.exception.UserBadRequestException;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.exception.ErrorCode;
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
    private final IdGenerator idGenerator;

    /**
     * todo
     * 생성시 orderNumber은 가장 마지막으로 배정
     * @param request
     * @return
     */
    public GetTicketResponse createTicket(CreateTicketRequest request, User allocatedUser) {
        // check User
        List<User> authUsers = projectService.getUserListByProject(request.projectId());

        checkUser(authUsers, allocatedUser);

        Project project = projectService.findProjectById(request.projectId());
        Integer lastOrderNum = ticketRepository.findLastOrderNumber(request.status(), request.projectId());
        String ticketId = idGenerator.generateId(IdPrefix.TICKET);
        Ticket ticket = Ticket.builder()
                .id(ticketId).project(project)
                .build();
        ticketRepository.save(ticket);

        if (!request.assignedUserIds().isEmpty()) {
            // assignedUser 추가
            List<User> assignedUsers = userService.getUsers(request.assignedUserIds());
            for (User assignedUser : assignedUsers) {
                ticketUserService.createTicketUser(ticket, assignedUser);
            }
        }

        return GetTicketResponse.builder().build();
    }

    public ListResponse<GetTicketResponse> getTicketList(String projectId) {
        List<Ticket> tickets = ticketRepository.findAllByProjectId(projectId);

        return new ListResponse(tickets);
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
    public ListResponse<GetTicketResponse> changeTickets(CreateTicketRequest request, User user, String ticketId) {
        // check User
        List<User> authUsers = projectService.getUserListByProject(request.projectId());
        checkUser(authUsers, user);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("존재하지 않는 티켓입니다."));

        boolean isEqualStatus = false;

        // 동일 status 인지 check
        TicketStatus preStatus = ticket.getStatus();    // 이전 ticket의 상태
        TicketStatus postStatus = checkStatus(request.status()); // 이후 ticket의 상태

        if (preStatus.equals(postStatus)) {
            isEqualStatus = true;
        }

        Integer preTicketOrderNum = ticket.getOrderNumber().getOrderNumber();    // 이전 ticket의 index 번호
        Integer postTicketOrderNum = request.orderNumber();  // 이후 ticket의 index 번호

        // assignedUser 추가
        if (!request.assignedUserIds().isEmpty()) {
            // 기존 assignedUser을 삭제 후 추가해야함
            ticket.deleteAssignedUsers();
            ticketUserService.deleteAssignedUser(ticket);
            List<User> assignedUsers = userService.getUsers(request.assignedUserIds());
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

    private ListResponse<GetTicketResponse> equalStatus(int preTicketOrderNum, int postTicketOrderNum, Ticket ticket, CreateTicketRequest request) {
        List<Ticket> changeTicketList = new ArrayList<>();
        if (postTicketOrderNum > preTicketOrderNum) {
            List<Ticket> tickets = ticketRepository.findAllLowToBigOrderNumber(request.projectId(), request.status(), preTicketOrderNum, postTicketOrderNum);
            decreaseOrderNum(tickets);
            Ticket updatedTicket = ticket.update(request, checkStatus(request.status()));
            changeTicketList.addAll(tickets);
            changeTicketList.add(updatedTicket);
        } else if (preTicketOrderNum > postTicketOrderNum) {
            List<Ticket> tickets = ticketRepository.findAllBigToLowOrderNumber(request.projectId(), request.status(), postTicketOrderNum, preTicketOrderNum);
            increaseOrderNum(tickets);
            Ticket updatedTicket = ticket.update(request, checkStatus(request.status()));
            changeTicketList.addAll(tickets);
            changeTicketList.add(updatedTicket);
        }
        return new ListResponse(changeTicketList);
//        return GetTicketResponse.from(changeTicketList);
    }

    private ListResponse<GetTicketResponse> notEqualStatus(int preTicketOrderNum, int postTicketOrderNum, Ticket ticket, CreateTicketRequest request,
                                                   TicketStatus preStatus, TicketStatus postStatus) {
        // status가 다를 경우
        // 이전 ticket들을 가져옴, orderNum이 pre 보다 큰
        List<Ticket> changeTicketList = new ArrayList<>();

        // 기존 status의 tickets
        List<Ticket> preStatusTickets = ticketRepository.findAllByBiggerOrderNumber(request.projectId(), preStatus.name(), preTicketOrderNum);

        // 요청된 ticket의 orderNum 보다 큰 orderNum을 가진 ticket들에 대해 -1
        decreaseOrderNum(preStatusTickets);
        changeTicketList.addAll(preStatusTickets);


        // 요청된 status의 tickets
        List<Ticket> postStatusTickets = ticketRepository.findAllByBiggerOrderNumber(request.projectId(), postStatus.name(), postTicketOrderNum);

        // 요청된 ticket의 orderNum 보다 작은 orderNum을 가진 ticket들에 대해 +1
        increaseOrderNum(postStatusTickets);
        changeTicketList.addAll(postStatusTickets);
        Ticket updatedTicket = ticket.update(request, checkStatus(request.status()));
        changeTicketList.add(updatedTicket);

        return new ListResponse(changeTicketList);
//        return GetTicketResponse.from(changeTicketList);
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
            if (authUser.getId().equals(user.getId())) {
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "잘못된 유저입니다.");
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

    public GetTicketResponse deleteTicket(String ticketId, User user) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("존재하지 않는 티켓입니다."));

        // check User
        List<User> authUsers = projectService.getUserListByProject(ticket.getProject().getId());
        checkUser(authUsers, user);

        ticketRepository.delete(ticket);

        return GetTicketResponse.builder().build();
//        return GetTicketResponse.from(ticket);
    }

    public void updateTicket(CreateTicketRequest request, User user, String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("존재하지 않는 티켓입니다."));
        // check User
        List<User> authUsers = projectService.getUserListByProject(ticket.getProject().getId());
        checkUser(authUsers, user);

        // assignedUser 수정
        if (!request.assignedUserIds().isEmpty()) {
            // 기존 assignedUser을 삭제 후 추가해야함
            ticket.deleteAssignedUsers();
            ticketUserService.deleteAssignedUser(ticket);

            List<User> assignedUsers = userService.getUsers(request.assignedUserIds());
            for (User assignedUser : assignedUsers) {
                ticketUserService.createTicketUser(ticket, assignedUser);
            }
        }

        ticket.update(request, checkStatus(request.status()));


//        return GetTicketResponse.from(ticket);
    }
}
