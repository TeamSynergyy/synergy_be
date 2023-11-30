package com.seoultech.synergybe.domain.rate.controller;

import com.seoultech.synergybe.domain.rate.dto.request.RateRequest;
import com.seoultech.synergybe.domain.rate.dto.response.RateResponse;
import com.seoultech.synergybe.domain.rate.dto.response.UserRateResponse;
import com.seoultech.synergybe.domain.rate.service.RateService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/rates")
@RequiredArgsConstructor
public class RateController {
    private final RateService rateService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RateResponse> createRate(@RequestBody RateRequest request, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.createRate(request, user));
    }

    @PutMapping("/{projectId}/evaluations")
    public ResponseEntity<List<UserRateResponse>> updateUserRate(@PathVariable("projectId") Long projectId, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(rateService.updateTemperature(projectId, user));
    }
}
