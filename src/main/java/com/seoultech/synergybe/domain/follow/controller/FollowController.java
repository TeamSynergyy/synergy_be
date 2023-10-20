package com.seoultech.synergybe.domain.follow.controller;

import com.seoultech.synergybe.domain.follow.dto.request.FollowType;
import com.seoultech.synergybe.domain.follow.dto.response.FollowResponse;
import com.seoultech.synergybe.domain.follow.service.FollowService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    private final UserService userService;

    @PutMapping(value = "/{followingId}")
    public ResponseEntity<ApiResponse<FollowResponse>> updateFollow(@PathVariable("followingId") String followingId, @RequestBody FollowType type) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("follow", followService.updateFollow(user, followingId,type)));

    }


}
