package com.seoultech.synergybe.domain.rate.controller;

import com.seoultech.synergybe.domain.rate.dto.request.RateRequest;
import com.seoultech.synergybe.domain.rate.dto.response.RateResponse;
import com.seoultech.synergybe.domain.rate.dto.response.UserRateResponse;
import com.seoultech.synergybe.domain.rate.service.RateService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/rates")
@RequiredArgsConstructor
@Tag(name = "프로젝트 평가 api")
public class RateController {
    private final RateService rateService;
    private final UserService userService;

    @Operation(summary = "평가 생성", description = "프로젝트 팀원에 대해 평가를 생성합니다.")
    @PostMapping
    public ResponseEntity<RateResponse> createRate(@RequestBody RateRequest request, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.createRate(request, user));
    }

    @Operation(summary = "평가 반영", description = "팀장만이 수행 가능하며, 평가가 진행된 내용에 대해 팀원 및 팀장에게 적용됩니다.")
    @PutMapping("/{projectId}/evaluations")
    public ResponseEntity<List<UserRateResponse>> updateUserRate(@PathVariable("projectId") Long projectId, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(rateService.updateTemperature(projectId, user));
    }

    @Operation(summary = "평가 목록", description = "프로젝트 구성원이 평가한 모든 평가들이 반환됩니다.")
    @GetMapping("/{projectId}")
    public ResponseEntity<List<RateResponse>> getRateList(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.ok().body(rateService.getRateListByProject(projectId));
    }
}
