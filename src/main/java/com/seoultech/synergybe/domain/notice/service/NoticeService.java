package com.seoultech.synergybe.domain.notice.service;

import com.seoultech.synergybe.domain.common.idgenerator.IdGenerator;
import com.seoultech.synergybe.domain.common.idgenerator.IdPrefix;
import com.seoultech.synergybe.domain.notice.Notice;
import com.seoultech.synergybe.domain.notice.dto.request.NoticeRequest;
import com.seoultech.synergybe.domain.notice.dto.response.NoticeResponse;
import com.seoultech.synergybe.domain.notice.exception.NoticeNotFoundException;
import com.seoultech.synergybe.domain.notice.repository.NoticeRepository;
import com.seoultech.synergybe.domain.notification.NotificationType;
import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final ProjectService projectService;
    private final NotificationService notificationService;
    private final IdGenerator idGenerator;


    public NoticeResponse createNotice(NoticeRequest request) {
        Project project = projectService.findProjectById(request.getProjectId());
        String noticeId = idGenerator.generateId(IdPrefix.NOTICE);
        Notice notice = Notice.builder()
                .id(noticeId).content(request.getContent()).project(project)
                .build();
        Notice savedNotice = this.noticeRepository.save(notice);
        List<User> projectUsers = project.getProjectUsers().stream().map(projectUser -> projectUser.getUser()).collect(Collectors.toList());
        for (User user : projectUsers) {
            notificationService.send(user, NotificationType.PROJECT_NOTICE, "공지사항이 생성되었습니다.", project.getId());
        }

        return NoticeResponse.from(savedNotice);

    }

    public NoticeResponse getNotice(Long notieId) {
        Notice notice = this.findNoticeById(notieId);

        return NoticeResponse.from(notice);

    }

    public Notice findNoticeById(Long noticeId) {
        return this.noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 공지입니다."));
    }

    public List<NoticeResponse> getNoticeList(Long projectId) {
        List<Long> noticeIds = noticeRepository.findNoticeIdsByProjectId(projectId);

        List<Notice> notices = noticeRepository.findAllById(noticeIds);

        // todo
        // update 순으로 정렬

        return NoticeResponse.from(notices);
    }

    public NoticeResponse deleteNotice(Long noticeId) {
        Notice notice = findNoticeById(noticeId);
        noticeRepository.delete(notice);

        return NoticeResponse.from(notice);
    }
}
