package com.seoultech.synergybe.domain.notice.service;

import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.notice.Notice;
import com.seoultech.synergybe.domain.notice.dto.request.CreateNoticeRequest;
import com.seoultech.synergybe.domain.notice.dto.response.GetNoticeResponse;
import com.seoultech.synergybe.domain.notice.exception.NoticeNotFoundException;
import com.seoultech.synergybe.domain.notice.repository.NoticeRepository;
import com.seoultech.synergybe.domain.notification.NotificationType;
import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.domain.project.domain.Project;
import com.seoultech.synergybe.domain.project.domain.service.ProjectService;
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


    public GetNoticeResponse createNotice(CreateNoticeRequest request) {
        Project project = projectService.findProjectById(request.projectId());
        String noticeId = idGenerator.generateId(IdPrefix.NOTICE);
        Notice notice = Notice.builder()
                .id(noticeId).content(request.content()).project(project)
                .build();
        Notice savedNotice = this.noticeRepository.save(notice);
        List<User> projectUsers = project.getProjectUsers().stream().map(projectUser -> projectUser.getUser()).collect(Collectors.toList());
        for (User user : projectUsers) {
            notificationService.send(user, NotificationType.PROJECT_NOTICE, "공지사항이 생성되었습니다.", project.getId());
        }
        GetNoticeResponse getNoticeResponse = GetNoticeResponse.builder().build();

        return getNoticeResponse;

    }

    public GetNoticeResponse getNotice(String noticeId) {
        Notice notice = this.findNoticeById(noticeId);

        GetNoticeResponse getNoticeResponse = GetNoticeResponse.builder().build();

        return getNoticeResponse;

    }

    public Notice findNoticeById(String noticeId) {
        return this.noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 공지입니다."));
    }

    public ListResponse<GetNoticeResponse> getNoticeList(String projectId) {
        List<String> noticeIds = noticeRepository.findNoticeIdsByProjectId(projectId);

        List<Notice> notices = noticeRepository.findAllById(noticeIds);

        // todo
        // update 순으로 정렬

        ListResponse<GetNoticeResponse> getNoticeResponses = new ListResponse(notices);



        return getNoticeResponses;
    }

    public void deleteNotice(String noticeId) {
        Notice notice = findNoticeById(noticeId);
        noticeRepository.delete(notice);
    }
}
