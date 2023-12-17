package com.seoultech.synergybe.domain.apply.controller;

import com.seoultech.synergybe.domain.apply.dto.request.ApplyRequest;
import com.seoultech.synergybe.domain.apply.dto.response.AcceptApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ListApplyUserResponse;
import com.seoultech.synergybe.domain.apply.dto.response.RejectApplyResponse;
import com.seoultech.synergybe.domain.apply.service.ApplyService;
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
@RequestMapping(value = "/api/v1/applies")
@RequiredArgsConstructor
@Tag(name = "프로젝트 지원 api")
public class ApplyController {
    private final ApplyService applyService;

    private final UserService userService;

    @Operation(summary = "프로젝트 지원 생성", description = "프로젝트와 user가 매핑되어 지원됩니다.")
    @PostMapping(value = "/{projectId}")
    public ResponseEntity<ApplyResponse> createApply(@PathVariable("projectId") Long projectId, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(applyService.createApply(user, projectId));
    }

    @Operation(summary = "프로젝트 지원 삭제", description = "지원이 삭제됩니다.")
    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<ApplyResponse> deleteApply(@PathVariable("projectId") Long projectId, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(applyService.deleteApply(user, projectId));
    }

    @Operation(summary = "프로젝트 지원 수락", description = "프로젝트의 팀장이 지원을 수락합니다.")
    @PostMapping(value = "/accept/{projectId}")
    public ResponseEntity<AcceptApplyResponse> acceptApply(@PathVariable("projectId") Long projectId, @RequestBody ApplyRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(applyService.acceptApply(request.getUserId(), projectId));
    }

    @Operation(summary = "프로젝트 지원 거절", description = "프로젝트의 팀장이 지원을 거절합니다.")
    @DeleteMapping("/reject/{projectId}")
    public ResponseEntity<RejectApplyResponse> rejectApply(@PathVariable("projectId") Long projectId, @RequestBody ApplyRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(applyService.rejectApply(request.getUserId(), projectId));
    }

    @Operation(summary = "내 프로젝트 지원 내역", description = "나의 프로젝트 지원 내역을 확인합니다.")
    @GetMapping(value = "/me")
    public ResponseEntity<List<ApplyResponse>> getApplyList(@LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(applyService.getMyApplyList(user));
    }

    @Operation(summary = "프로젝트 지원 유저 목록", description = "프로젝트에 지원한 유저의 목록입니다.")
    @GetMapping(value = "/{projectId}")
    public ResponseEntity<ListApplyUserResponse> getListApplyUser(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(applyService.getApplyUserList(projectId));
    }
}
