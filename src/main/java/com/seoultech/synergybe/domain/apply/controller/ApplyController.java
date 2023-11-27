package com.seoultech.synergybe.domain.apply.controller;

import com.seoultech.synergybe.domain.apply.dto.request.ApplyRequest;
import com.seoultech.synergybe.domain.apply.dto.response.AcceptApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ListApplyUserResponse;
import com.seoultech.synergybe.domain.apply.dto.response.RejectApplyResponse;
import com.seoultech.synergybe.domain.apply.service.ApplyService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
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
    public ResponseEntity<ApplyResponse> createApply(@PathVariable("projectId") Long projectId) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(applyService.createApply(user, projectId));
    }

    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<ApplyResponse> deleteApply(@PathVariable("projectId") Long projectId) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(applyService.deleteApply(user, projectId));
    }

    @PostMapping(value = "/accept/{projectId}")
    public ResponseEntity<AcceptApplyResponse> acceptApply(@PathVariable("projectId") Long projectId, @RequestBody ApplyRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(applyService.acceptApply(request.getUserId(), projectId));
    }

    @DeleteMapping("/reject/{projectId}")
    public ResponseEntity<RejectApplyResponse> rejectApply(@PathVariable("projectId") Long projectId, @RequestBody ApplyRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(applyService.rejectApply(request.getUserId(), projectId));
    }

    @GetMapping(value = "/me")
    public ResponseEntity<List<ApplyResponse>> getApplyList() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(applyService.getMyApplyList(user));
    }

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<ListApplyUserResponse> getListApplyUser(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(applyService.getApplyUserList(projectId));
    }
}
