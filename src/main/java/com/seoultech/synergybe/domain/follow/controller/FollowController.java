package com.seoultech.synergybe.domain.follow.controller;

import com.seoultech.synergybe.domain.follow.dto.request.FollowType;
import com.seoultech.synergybe.domain.follow.dto.response.FollowResponse;
import com.seoultech.synergybe.domain.follow.service.FollowService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    private final UserService userService;

    @PutMapping(value = "/{followingId}")
    public ResponseEntity<FollowResponse> updateFollow(@PathVariable("followingId") String followingId, @RequestBody FollowType type, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(followService.updateFollow(user, followingId, type));

    }


}
