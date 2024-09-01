package com.seoultech.synergybe.domain.apply.controller;

import com.seoultech.synergybe.domain.apply.dto.response.*;
import com.seoultech.synergybe.domain.apply.service.ApplyService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1/applies")
@RequiredArgsConstructor
@Tag(name = "프로젝트 지원 api")
public class ApplyController {
    private final ApplyService applyService;

    @Operation(summary = "프로젝트 지원 생성", description = "프로젝트와 user가 매핑되어 지원됩니다.")
    @PostMapping(value = "/{projectToken}")
    public ResponseEntity<GetApplyResponse> createApply(@PathVariable("projectToken") String projectToken, @LoginUser String userToken) {

        return ResponseEntity.status(HttpStatus.CREATED).body(applyService.createApply(userToken, projectToken));
    }

    @Operation(summary = "프로젝트 지원 삭제", description = "지원이 삭제됩니다.")
    @DeleteMapping(value = "/{applyId}")
    public ResponseEntity<Void> deleteApply(@PathVariable("applyId") String applyId, @LoginUser String userId) {
        applyService.deleteApply(userId, applyId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프로젝트 지원 수락", description = "프로젝트의 팀장이 지원을 수락합니다.")
    @PostMapping(value = "/accept/{projectId}")
    public ResponseEntity<Void> acceptApply(@PathVariable("projectId") String projectId, @RequestParam("applyUserId") String applyUserId, @LoginUser String leaderId) {
        applyService.updateApplyStatusToAccept(leaderId, applyUserId, projectId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프로젝트 지원 거절", description = "프로젝트의 팀장이 지원을 거절합니다.")
    @DeleteMapping("/reject/{projectId}")
    public ResponseEntity<Void> rejectApply(@PathVariable("projectId") String projectId, @RequestParam("applyUserId") String applyUserId, @LoginUser String leaderId) {
        applyService.updateApplyStatusToReject(leaderId, applyUserId, projectId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내 프로젝트 지원 내역", description = "나의 프로젝트 지원 내역을 확인합니다.")
    @GetMapping(value = "/me")
    public ResponseEntity<GetListApplyResponse> getApplyList(@LoginUser String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(applyService.getMyApplyList(userId));
    }

    @Operation(summary = "프로젝트 지원 유저 목록", description = "프로젝트에 지원한 유저의 목록입니다.")
    @GetMapping(value = "/{projectId}")
    public ResponseEntity<GetListApplyUserResponse> getListApplyUser(@PathVariable("projectId") String projectId) {

        return ResponseEntity.status(HttpStatus.OK).body(applyService.getApplyUserList(projectId));
    }
}
