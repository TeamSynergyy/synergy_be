package com.seoultech.synergybe.domain.rate.controller;

import com.seoultech.synergybe.domain.rate.dto.request.RateRequest;
import com.seoultech.synergybe.domain.rate.dto.response.RateResponse;
import com.seoultech.synergybe.domain.rate.service.RateService;
import com.seoultech.synergybe.system.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/rates")
@RequiredArgsConstructor
public class RateController {
    private final RateService rateService;

    @PostMapping
    public ResponseEntity<ApiResponse<RateResponse>> createRate(@RequestBody RateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.create("rate create", rateService.createRate(request)));
    }
}
