package com.seoultech.synergybe.domain.rate.controller;

import com.seoultech.synergybe.domain.rate.dto.request.RateRequest;
import com.seoultech.synergybe.domain.rate.dto.response.RateResponse;
import com.seoultech.synergybe.domain.rate.dto.response.UserRateResponse;
import com.seoultech.synergybe.domain.rate.service.RateService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/rates")
@RequiredArgsConstructor
public class RateController {
    private final RateService rateService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<RateResponse>> createRate(@RequestBody RateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.create("rate create", rateService.createRate(request)));
    }

    @PutMapping("/{projectId}/evaluate-user")
    public ResponseEntity<ApiResponse<UserRateResponse>> updateUserRate(@PathVariable("projectId") Long projectId) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("rate update", rateService.updateTemperature(projectId, user)));
    }
}
