package com.seoultech.synergybe.domain.notice.dto.response;

import com.seoultech.synergybe.domain.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
public class NoticeResponse {

    private Long noticeId;
    private String content;

    private LocalDateTime updateAt;

    public NoticeResponse(Long contentId, String content) {
        this.noticeId = contentId;
        this.content = content;
    }

    public static NoticeResponse from(Notice notice) {
        return new NoticeResponse(notice.getId(), notice.getContent());
    }

    public static List<NoticeResponse> from(List<Notice> notices) {
        return notices.stream()
                .map(notice -> NoticeResponse.builder()
                        .noticeId(notice.getId())
                        .content(notice.getContent())
                        .updateAt(notice.getUpdateAt())
                        .build())
                .collect(Collectors.toList());
    }
}
