package com.seoultech.synergybe.domain.follow.controller;

import com.seoultech.synergybe.domain.follow.dto.request.FollowType;
import com.seoultech.synergybe.domain.follow.dto.response.FollowResponse;
import com.seoultech.synergybe.domain.follow.service.FollowService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/follows")
@RequiredArgsConstructor
@Tag(name = "팔로우 api")
public class FollowController {
    private final FollowService followService;

    private final UserService userService;

    @Operation(summary = "follow 신청, 취소", description = "팔로우를 신청 및 취소하며 팔로우 타입에 따라 팔로우 상태가 변화하므로 PUT 메서드 하나로 관리됩니다.")
    @PutMapping(value = "/{followingId}")
    public ResponseEntity<FollowResponse> updateFollow(@PathVariable("followingId") String followingId, @RequestBody FollowType type, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(followService.updateFollow(user, followingId, type));

    }


}
