package com.seoultech.synergybe.domain.apply.service;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.dto.response.AcceptApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ListApplyUserResponse;
import com.seoultech.synergybe.domain.apply.dto.response.RejectApplyResponse;
import com.seoultech.synergybe.domain.apply.repository.ApplyRepository;
import com.seoultech.synergybe.domain.notification.NotificationType;
import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.projectuser.ProjectUser;
import com.seoultech.synergybe.domain.projectuser.repository.ProjectUserRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.exception.NotExistApplyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public ApplyResponse createApply(User user, Long projectId) {
        Project project = projectService.findProjectById(projectId);

        Apply apply = Apply.builder()
                .user(user).project(project).build();
        Apply savedApply = applyRepository.save(apply);

        // 리더에게 알림
        User leader = userService.getUser(projectService.getProject(projectId).getLeaderId());
        notificationService.send(leader, NotificationType.PROJECT_APPLY, "프로젝트 신청이 완료되었습니다.", projectId);

        return ApplyResponse.from(savedApply);
    }

    public ApplyResponse deleteApply(User user, Long projectId) {
        Optional<Apply> applyOptional = applyRepository.findByUserIdAndProjectId(user.getUserId(), projectId);

        if (applyOptional.isPresent()) {
            applyRepository.delete(applyOptional.get());

            return ApplyResponse.from(applyOptional.get());
        } else {
            throw new NotExistApplyException();
        }
    }

    public AcceptApplyResponse acceptApply(String userId, Long projectId) {
        Apply apply = applyRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(NotExistApplyException::new);

        apply.accepted();
        Project project = projectService.findProjectById(projectId);
        User user = userService.getUser(userId);


        // projectUser 추가
        ProjectUser projectUser = new ProjectUser(project, user);
        project.getProjectUsers().add(projectUser);
        projectUserRepository.save(projectUser);
        User applyUser = userService.getUser(userId);

        // apply 삭제
//        applyRepository.delete(apply);

        // 알림 발송
        notificationService.send(applyUser, NotificationType.PROJECT_ACCEPT, "신청이 수락되었습니다.", projectId);

        return AcceptApplyResponse.from(apply);
    }

    public RejectApplyResponse rejectApply(String userId, Long projectId) {
        Apply apply = applyRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(NotExistApplyException::new);
        apply.rejected();

        // apply 삭제
        applyRepository.delete(apply);

        // 알림 발송
        User applyUser = userService.getUser(userId);
        notificationService.send(applyUser, NotificationType.PROJECT_REJECT, "신청이 거절되었습니다.", projectId);

        return RejectApplyResponse.from(apply);
    }

    public List<ApplyResponse> getMyApplyList(User user) {
        List<Apply> applies = applyRepository.findAllProcessByUserId(user.getUserId());

        return ApplyResponse.from(applies);

    }

    public ListApplyUserResponse getApplyUserList(Long projectId) {
        List<String> userIds = applyRepository.findUserIdsByProjectId(projectId);

        // user_id 는 PK가 아닌 UNIQUE KEY 이므로 findAllById() 사용 못함
        List<User> users = userService.getUsers(userIds);

        return ListApplyUserResponse.from(users);
    }
}
