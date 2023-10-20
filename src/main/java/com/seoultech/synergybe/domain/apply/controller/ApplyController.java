package com.seoultech.synergybe.domain.apply.controller;

import com.seoultech.synergybe.domain.apply.dto.request.ApplyRequest;
import com.seoultech.synergybe.domain.apply.dto.response.AcceptApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ListApplyUserResponse;
import com.seoultech.synergybe.domain.apply.dto.response.RejectApplyResponse;
import com.seoultech.synergybe.domain.apply.service.ApplyService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/applies")
@RequiredArgsConstructor
public class ApplyController {
    private final ApplyService applyService;

    private final UserService userService;

    @PostMapping(value = "/{projectId}")
    public ResponseEntity<ApiResponse<ApplyResponse>> createApply(@PathVariable("projectId") Long projectId) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.create("project apply create", applyService.createApply(user, projectId)));
    }

    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<ApiResponse<ApplyResponse>> deleteApply(@PathVariable("projectId") Long projectId) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("project apply delete", applyService.deleteApply(user, projectId)));
    }

    @PostMapping(value = "/accept/{projectId}")
    public ResponseEntity<ApiResponse<AcceptApplyResponse>> acceptApply(@PathVariable("projectId") Long projectId, ApplyRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("accept apply", applyService.acceptApply(request.getUserId(), projectId)));
    }

    @DeleteMapping("/reject/{projectId}")
    public ResponseEntity<ApiResponse<RejectApplyResponse>> rejectApply(@PathVariable("projectId") Long projectId, ApplyRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("reject apply", applyService.rejectApply(request.getUserId(), projectId)));
    }

    @GetMapping(value = "/me")
    public ResponseEntity<ApiResponse<List<ApplyResponse>>> getApplyList() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("me apply list", applyService.getMyApplyList(user)));
    }

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<ApiResponse<ListApplyUserResponse>> getListApplyUser(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("apply user list", applyService.getApplyUserList(projectId)));
    }
}
