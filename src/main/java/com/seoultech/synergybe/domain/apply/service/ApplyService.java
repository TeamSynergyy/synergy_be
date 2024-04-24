package com.seoultech.synergybe.domain.apply.service;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.dto.response.*;
import com.seoultech.synergybe.domain.apply.exception.ApplyNotFoundException;
import com.seoultech.synergybe.domain.apply.repository.ApplyRepository;
import com.seoultech.synergybe.domain.common.idgenerator.IdGenerator;
import com.seoultech.synergybe.domain.common.idgenerator.IdPrefix;
import com.seoultech.synergybe.domain.notification.NotificationType;
import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.projectuser.ProjectUser;
import com.seoultech.synergybe.domain.projectuser.repository.ProjectUserRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final ProjectService projectService;
    private final ProjectUserRepository projectUserRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final IdGenerator idGenerator;

    public GetApplyResponse createApply(User user, String projectId) {
        Project project = projectService.findProjectById(projectId);
        String applyId = idGenerator.generateId(IdPrefix.APPLY);

        Apply apply = Apply.builder()
                .id(applyId).user(user).project(project)
                .build();
        Apply savedApply = applyRepository.save(apply);

        // 리더에게 알림
//        User leader = userService.getUser(projectService.getProject(projectId).leaderId());
//        notificationService.send(leader, NotificationType.PROJECT_APPLY, "프로젝트 신청이 완료되었습니다.", projectId);

        GetApplyResponse getApplyResponse = GetApplyResponse.builder().build();

        return getApplyResponse;
    }

    public void deleteApply(String userId, String projectId) {
        Optional<Apply> applyOptional = applyRepository.findApplyByUserIdAndProjectId(userId, projectId);

        if (applyOptional.isPresent()) {
            applyRepository.delete(applyOptional.get());

        } else {
            throw new ApplyNotFoundException("존재하지 않는 신청내역입니다.");
        }
    }

    public void acceptApply(String userId, String projectId) {
        Apply apply = applyRepository.findApplyByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new ApplyNotFoundException("존재하지 않는 신청내역입니다."));

        apply.changeStatusToAccept();
        Project project = projectService.findProjectById(projectId);
        User user = userService.getUser(userId);


        // projectUser 추가
        String projectUserId = idGenerator.generateId(IdPrefix.PROJECT_USER);
        ProjectUser projectUser = new ProjectUser(projectUserId, project, user);
        project.getProjectUsers().add(projectUser);
        projectUserRepository.save(projectUser);
        User applyUser = userService.getUser(userId);

        // apply 삭제
//        applyRepository.delete(apply);

        // 알림 발송
//        notificationService.send(applyUser, NotificationType.PROJECT_ACCEPT, "신청이 수락되었습니다.", projectId);
    }

    public void rejectApply(String userId, String projectId) {
        Apply apply = applyRepository.findApplyByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new ApplyNotFoundException("존재하지 않는 신청내역입니다."));
        apply.changeStatusToReject();

        // apply 삭제
        applyRepository.delete(apply);

        // 알림 발송
        User applyUser = userService.getUser(userId);
        notificationService.send(applyUser, NotificationType.PROJECT_REJECT, "신청이 거절되었습니다.", projectId);
//
//        return RejectApplyResponse.from(apply);
    }

    public GetListApplyResponse getMyApplyList(User user) {
        List<Apply> applies = applyRepository.findAllProcessByUserId(user.getUserId());

        GetListApplyResponse getListApplyResponse = GetListApplyResponse.builder().build();

        return getListApplyResponse;

    }

    public GetListApplyUserResponse getApplyUserList(String projectId) {
        List<String> userIds = applyRepository.findUserIdsByProjectId(projectId);

        // user_id 는 PK가 아닌 UNIQUE KEY 이므로 findAllById() 사용 못함
        List<User> users = userService.getUsers(userIds);

        GetListApplyUserResponse getListApplyUserResponse = GetListApplyUserResponse.builder().build();

        return getListApplyUserResponse;
    }
}
