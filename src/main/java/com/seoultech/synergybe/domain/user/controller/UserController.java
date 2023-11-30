package com.seoultech.synergybe.domain.user.controller;

import com.seoultech.synergybe.domain.user.dto.request.UpdateUserRequest;
import com.seoultech.synergybe.domain.user.dto.response.ListUserResponse;
import com.seoultech.synergybe.domain.user.dto.response.UserIdsResponse;
import com.seoultech.synergybe.domain.user.dto.response.UserResponse;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "유저 api", description = "유저 관련 api 입니다")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/me/info")
    public ResponseEntity<UserResponse> getMyInfo(@LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userService.getMyInfo(user));
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(userId));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> searchAllPosts(@RequestParam("search") String search, @PageableDefault(size = 15) Pageable pageable) {
        log.info(">> keyword : {}", search);

        return ResponseEntity.status(HttpStatus.OK).body(userService.searchAllUsers(search, pageable));
    }

    @PutMapping(value = "/me/info")
    public ResponseEntity<UserResponse> updateMyInfo(@RequestBody UpdateUserRequest request, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userService.updateMyInfo(user, request));
    }

    @GetMapping(value = "/similar/{userId}")
    public ResponseEntity<ListUserResponse> getSimilarUsers(@PathVariable("userId") String userId, @RequestParam(value = "end", required = false, defaultValue = "0") Long end) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getSimilarUserListByUser(userId, end));
    }

    @GetMapping(value = "/followers")
    public ResponseEntity<UserIdsResponse> getFollowerIds(@LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userService.getFollowerIds(user));
    }

    @GetMapping(value = "/followings")
    public ResponseEntity<UserIdsResponse> getFollowingIds(@LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userService.getFollowingIds(user));
    }
}

