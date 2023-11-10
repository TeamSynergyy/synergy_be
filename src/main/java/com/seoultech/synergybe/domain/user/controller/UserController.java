package com.seoultech.synergybe.domain.user.controller;

import com.seoultech.synergybe.domain.user.dto.request.UpdateUserRequest;
import com.seoultech.synergybe.domain.user.dto.response.ListUserResponse;
import com.seoultech.synergybe.domain.user.dto.response.UserResponse;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.common.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "유저 api", description = "유저 관련 api 입니다")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/me/info")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("user info", userService.getMyInfo(user)));
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable("userId") String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("user info", userService.getUserInfo(userId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchAllPosts(@RequestParam("search") String search, @PageableDefault(size = 15) Pageable pageable) {
        log.info(">> keyword : {}", search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("search user list", userService.searchAllUsers(search, pageable)));
    }

    @PutMapping(value = "/me/info")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyInfo(@RequestBody UpdateUserRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("update my info", userService.updateMyInfo(user, request)));
    }

    @GetMapping(value = "/recommend")
    public ResponseEntity<ApiResponse<ListUserResponse>> getRecommendUsers(@RequestParam(value = "end", required = false, defaultValue = "0") Long end) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("recommend users", userService.getRecommendListByUser(user, end)));
    }
}

