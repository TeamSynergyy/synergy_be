package com.seoultech.synergybe.domain.notice.dto.response;

import com.seoultech.synergybe.domain.notice.Notice;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record GetNoticeResponse(
        String noticeId,
        String content,
        LocalDateTime updateAt,
        String projectId

) {

//    public static GetNoticeResponse from(Notice notice) {
//        return GetNoticeResponse.builder()
//                .noticeId(notice.getId())
//                .content(notice.getContent())
//                .updateAt(notice.getUpdateAt())
//                .projectId(notice.getProject().getId())
//                .build();
//    }
//
//    public static List<GetNoticeResponse> from(List<Notice> notices) {
//        return notices.stream()
//                .map(notice -> GetNoticeResponse.builder()
//                        .noticeId(notice.getId())
//                        .content(notice.getContent())
//                        .updateAt(notice.getUpdateAt())
//                        .build())
//                .collect(Collectors.toList());
//    }
}
