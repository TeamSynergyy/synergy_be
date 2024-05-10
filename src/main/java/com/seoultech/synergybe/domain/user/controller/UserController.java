package com.seoultech.synergybe.domain.user.controller;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.user.dto.request.CreateUserRequest;
import com.seoultech.synergybe.domain.user.dto.request.UpdateUserRequest;
import com.seoultech.synergybe.domain.user.dto.response.CreateUserResponse;
import com.seoultech.synergybe.domain.user.dto.response.GetUserAccountResponse;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.config.login.LoginUser;
import com.seoultech.synergybe.system.utils.EmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "유저 api")
public class UserController {

    private final UserService userService;
    @Operation(summary = "나의 정보", description = "내 프로필 정보가 반환됩니다.")
    @GetMapping(value = "/me/info")
    public ResponseEntity<GetUserAccountResponse> getMyInfo(@LoginUser String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(userId));
    }

    @PostMapping("/email-auth")
    public ResponseEntity<Void> validateEmail(@Valid @RequestBody EmailRequest request) {
        userService.validateEmail(request);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "유저 조회", description = "유저Id 기준으로 해당 유저를 반환합니다.")
    @GetMapping(value = "/{userId}")
    public ResponseEntity<GetUserAccountResponse> getUser(@PathVariable("userId") String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(userId));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request.email(), request.password(), request.name(), request.major()));
    }

    @Operation(summary = "검색어를 포함하는 유자", description = "검색어를 포함하는 유저가 반환됩니다.")
    @GetMapping
    public ResponseEntity<Page<User>> searchAllPosts(@RequestParam("search") String search, @PageableDefault(size = 15) Pageable pageable) {
        log.info(">> keyword : {}", search);

        return ResponseEntity.status(HttpStatus.OK).body(userService.searchAllUsers(search, pageable));
    }

    @Operation(summary = "내 정보 수정", description = "요청된 내용에 따라 내 정보가 수정됩니다.")
    @PutMapping(value = "/me/info")
    public ResponseEntity<Void> updateMyInfo(@Valid @RequestBody UpdateUserRequest request, @LoginUser String userId) {
        userService.updateMyInfo(userId, request.email(), request.name(), request.major());

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "유저 추천", description = "나의 활동을 바탕으로 유저가 추천됩니다.")
    @GetMapping(value = "/similar/{userId}")
    public ResponseEntity<ListResponse<GetUserAccountResponse>> getSimilarUsers(@PathVariable("userId") String userId, @RequestParam(value = "end", required = false, defaultValue = "0") Long end) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getSimilarUserListByUser(userId, end));
    }

//    @Operation(summary = "나의 팔로워 목록", description = "나를 팔로우 하고있는 유저의 Id 목록을 반환합니다")
//    @GetMapping(value = "/followers")
//    public ResponseEntity<ListResponse<String>> getFollowerIds(@LoginUser String userId) {
//
//        return ResponseEntity.status(HttpStatus.OK).body(userService.getFollowerIds(userId));
//    }
//
//    @Operation(summary = "나의 팔로잉 목록", description = "내가 팔로우 하고있는 유저의 Id 목록을 반환합니다")
//    @GetMapping(value = "/followings")
//    public ResponseEntity<ListResponse<String>> getFollowingIds(@LoginUser String userId) {
//
//        return ResponseEntity.status(HttpStatus.OK).body(userService.getFollowingIds(userId));
//    }
}

