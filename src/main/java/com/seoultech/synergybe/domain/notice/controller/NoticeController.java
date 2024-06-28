package com.seoultech.synergybe.domain.notice.controller;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.notice.dto.request.CreateNoticeRequest;
import com.seoultech.synergybe.domain.notice.dto.response.GetNoticeResponse;
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
    public ResponseEntity<GetNoticeResponse> createNotice(@RequestBody CreateNoticeRequest request, @LoginUser String userId) {


        return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.createNotice(request));
    }

    @Operation(summary = "공지 조회", description = "프로젝트 공지를 단건 조회합니다.")
    @GetMapping(value = "/{projectToken}")
    public ResponseEntity<ListResponse<GetNoticeResponse>> getNotice(@PathVariable("projectToken") String projectToken) {

        return ResponseEntity.status(HttpStatus.OK).body(noticeService.getNoticeList(projectToken));
    }

    @Operation(summary = "공지 삭제", description = "프로젝트 공지를 삭제합니다.")
    @DeleteMapping("/{noticeToken}")
    public ResponseEntity<GetNoticeResponse> deleteNotice(@PathVariable("noticeToken") String noticeToken, @LoginUser String userId) {
        noticeService.deleteNotice(noticeToken);
        return ResponseEntity.noContent().build();
    }

}
