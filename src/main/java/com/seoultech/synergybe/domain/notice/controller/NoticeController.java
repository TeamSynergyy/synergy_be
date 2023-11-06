package com.seoultech.synergybe.domain.notice.controller;

import com.seoultech.synergybe.domain.notice.dto.request.NoticeRequest;
import com.seoultech.synergybe.domain.notice.dto.response.NoticeResponse;
import com.seoultech.synergybe.domain.notice.service.NoticeService;
import com.seoultech.synergybe.system.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<ApiResponse<NoticeResponse>> createNotice(@RequestBody NoticeRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.create("notice", this.noticeService.createNotice(request)));
    }

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<ApiResponse<List<NoticeResponse>>> getNotice(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("notice list", this.noticeService.getNoticeList(projectId)));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeResponse>> deleteNotice(@PathVariable("noticeId") Long noticeId) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("notice delete", this.noticeService.deleteNotice(noticeId)));
    }

}
