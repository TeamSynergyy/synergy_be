package com.seoultech.synergybe.domain.user.controller;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.user.application.UserFacade;
import com.seoultech.synergybe.domain.user.dto.request.CreateUserRequest;
import com.seoultech.synergybe.domain.user.dto.request.UpdateUserRequest;
import com.seoultech.synergybe.domain.user.dto.request.ValidateNumberRequest;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private final UserFacade userFacade;

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest request) {
        String userToken = userFacade.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(userToken);
    }

    @PostMapping("/sending-number")
    public ResponseEntity<Void> sendValidationNumber(@Valid @RequestBody ValidateNumberRequest request) {
        userFacade.sendValidationNumber(request.email());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validation-email")
    public ResponseEntity<Void> validateEmail(@Valid @RequestBody ValidateNumberRequest request) {
        userFacade.validateNumber(request);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "나의 정보", description = "내 프로필 정보가 반환됩니다.")
    @GetMapping(value = "/me/info")
    public ResponseEntity<GetUserResponse> getMyInfo(@LoginUser String userToken) {
        GetUserResponse response = userFacade.getUserInfo(userToken);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "유저 조회", description = "유저Id 기준으로 해당 유저를 반환합니다.")
    @GetMapping(value = "/{userId}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable("userId") String userToken) {
        GetUserResponse response = userFacade.getUserInfo(userToken);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "검색어를 포함하는 유자", description = "검색어를 포함하는 유저가 반환됩니다.")
    @GetMapping
    public ResponseEntity<Page<User>> searchAllUsers(@RequestParam("search") String search, @PageableDefault(size = 15) Pageable pageable) {
        Page<User> response = userFacade.searchAllUsers(search, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "내 정보 수정", description = "요청된 내용에 따라 내 정보가 수정됩니다.")
    @PutMapping(value = "/me/info")
    public ResponseEntity<Void> updateMyInfo(@Valid @RequestBody UpdateUserRequest request, @LoginUser String userToken) {
        userFacade.updateMyInfo(userToken, request);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "유저 추천", description = "나의 활동을 바탕으로 유저가 추천됩니다.")
    @GetMapping(value = "/similar/{userId}")
    public ResponseEntity<ListResponse<GetUserResponse>> getSimilarUsers(@PathVariable("userToken") String userToken, @RequestParam(value = "end", required = false, defaultValue = "0") Long end) {
        ListResponse<GetUserResponse> response = userFacade.getSimilarUsers(userToken, end);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<Void> generateAccessTokenByRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        userFacade.generateAccessTokenByRefreshToken(request, response);

        return ResponseEntity.noContent().build();
    }
}

