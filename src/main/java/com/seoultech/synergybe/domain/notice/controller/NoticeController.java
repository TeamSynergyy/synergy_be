package com.seoultech.synergybe.domain.notice.controller;

import com.seoultech.synergybe.domain.notice.dto.request.NoticeRequest;
import com.seoultech.synergybe.domain.notice.dto.response.NoticeResponse;
import com.seoultech.synergybe.domain.notice.service.NoticeService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/notices")
@RequiredArgsConstructor
@Tag(name = "공지 api")
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(summary = "공지 생성", description = "공지가 생성되며 모든 프로젝트 유저들에게 알림을 전송합니다.")
    @PostMapping
    public ResponseEntity<NoticeResponse> createNotice(@RequestBody NoticeRequest request, @LoginUser String userId) {


        return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.createNotice(request));
    }

    @Operation(summary = "공지 조회", description = "프로젝트 공지를 단건 조회합니다.")
    @GetMapping(value = "/{projectId}")
    public ResponseEntity<List<NoticeResponse>> getNotice(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.status(HttpStatus.OK).body(noticeService.getNoticeList(projectId));
    }

    @Operation(summary = "공지 삭제", description = "프로젝트 공지를 삭제합니다.")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> deleteNotice(@PathVariable("noticeId") Long noticeId, @LoginUser String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(noticeService.deleteNotice(noticeId));
    }

}
