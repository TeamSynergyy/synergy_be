package com.seoultech.synergybe.domain.notice.controller;

import com.seoultech.synergybe.domain.notice.dto.request.NoticeRequest;
import com.seoultech.synergybe.domain.notice.dto.response.NoticeResponse;
import com.seoultech.synergybe.domain.notice.service.NoticeService;
import com.seoultech.synergybe.system.config.login.LoginUser;
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
    public ResponseEntity<NoticeResponse> createNotice(@RequestBody NoticeRequest request, @LoginUser String userId) {


        return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.createNotice(request));
    }

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<List<NoticeResponse>> getNotice(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.status(HttpStatus.OK).body(noticeService.getNoticeList(projectId));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> deleteNotice(@PathVariable("noticeId") Long noticeId, @LoginUser String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(noticeService.deleteNotice(noticeId));
    }

}
