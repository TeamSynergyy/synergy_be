package com.seoultech.synergybe.domain.ticket.controller;

import com.seoultech.synergybe.domain.ticket.dto.TicketRequest;
import com.seoultech.synergybe.domain.ticket.dto.TicketResponse;
import com.seoultech.synergybe.domain.ticket.service.TicketService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/tickets")
@Tag(name = "작업 api")
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;

    @Operation(summary = "작업 생성", description = "작업을 생성하며, 할당된 유저(여러명)들을 포함합니다.")
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest request, @LoginUser String userId) {
        User allocatedUser = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(request, allocatedUser));
    }

    @Operation(summary = "작업 조회", description = "프로젝트의 모든 작업 목록을 반환합니다.")
    @GetMapping(value = "/{projectId}")
    public ResponseEntity<List<TicketResponse>> getTicketList(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.ok().body(ticketService.getTicketList(projectId));
    }

    @Operation(summary = "작업 수정", description = "요청된 내용에 따라 작업이 수정됩니다.")
    @PutMapping(value = "/{ticketId}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable("ticketId") Long ticketId, @LoginUser String userId,
                                                       @RequestBody TicketRequest request) {
        User user = userService.findUserById(userId);

        return ResponseEntity.ok().body(ticketService.updateTicket(request, user, ticketId));
    }

    @Operation(summary = "작업 상태변경", description = "작업이 진행됨에 따라 작업의 상태를 칸반보드 형식으로 변경이 가능합니다.")
    @PutMapping(value = "/change/{ticketId}")
    public ResponseEntity<List<TicketResponse>> changeTicket(@PathVariable("ticketId") Long ticketId, @LoginUser String userId,
                                                       @RequestBody TicketRequest request) {
        User allocatedUser = userService.findUserById(userId);

        return ResponseEntity.ok().body(ticketService.changeTickets(request, allocatedUser, ticketId));
    }

    @Operation(summary = "작업 삭제", description = "작업을 삭제합니다.")
    @DeleteMapping(value = "/{ticketId}")
    public ResponseEntity<TicketResponse> deleteTicket(@PathVariable("ticketId") Long ticketId, @LoginUser String userId) {
        User allocatedUser = userService.findUserById(userId);

        return ResponseEntity.ok().body(ticketService.deleteTicket(ticketId, allocatedUser));
    }
}
