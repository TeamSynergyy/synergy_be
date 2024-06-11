package com.seoultech.synergybe.domain.apply.service;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.dto.response.*;
import com.seoultech.synergybe.domain.apply.exception.ApplyBadRequestException;
import com.seoultech.synergybe.domain.apply.exception.ApplyNotFoundException;
import com.seoultech.synergybe.domain.apply.repository.ApplyRepository;
import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.generator.TokenGenerator;
import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.domain.project.domain.Project;
import com.seoultech.synergybe.domain.project.domain.service.ProjectService;
import com.seoultech.synergybe.domain.projectuser.ProjectUser;
import com.seoultech.synergybe.domain.projectuser.repository.ProjectUserRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final ProjectService projectService;
    private final ProjectUserRepository projectUserRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final IdGenerator idGenerator;
    private final TokenGenerator tokenGenerator;

    @Transactional
    public GetApplyResponse createApply(String userToken, String projectToken) {
        Project project = projectService.findProjectById(projectToken);
        User user = userService.getUser(userToken);
        Long applyId = idGenerator.generateId();
        String applyToken = tokenGenerator.generateToken(IdPrefix.APPLY);

        Apply apply = Apply.builder()
                .id(applyId)
                .applyToken(applyToken)
                .user(user)
                .project(project)
                .build();
        Apply savedApply = applyRepository.save(apply);

        // 리더에게 알림
//        User leader = userService.getUser(projectService.getProject(projectId).leaderId());
//        notificationService.send(leader, NotificationType.PROJECT_APPLY, "프로젝트 신청이 완료되었습니다.", projectId);

        return GetApplyResponse.builder()
                .applyToken(savedApply.getApplyToken())
                .build();
    }

    @Transactional
    public void deleteApply(String userId, String applyId) {
        // todo
        // 사용자 권한 검증
        User user = userService.getUser(userId);
        Apply apply = getApply(applyId);
        validateApplyUser(user, apply);
        applyRepository.delete(apply);
    }

    private void validateApplyUser(User user, Apply apply) {
        if (!apply.getUser().equals(user)) {
            throw new ApplyBadRequestException("지원내역에 대한 권한이 없습니다.");
        }
    }

    @Transactional
    public void updateApplyStatusToAccept(String leaderId, String applyUserId, String projectId) {
        Apply apply = applyRepository.findApplyByUserIdAndProjectId(applyUserId, projectId);
        Project project = projectService.findProjectById(projectId);
        // 팀장 검증
        validateLeader(leaderId, project);

        log.info("applyId : " + apply.getId());
        apply.changeStatusToAccept();
        User user = userService.getUser(applyUserId);


        // projectUser 추가
        Long projectUserId = idGenerator.generateId();
        String projectUserToken = tokenGenerator.generateToken(IdPrefix.PROJECT_USER);
        ProjectUser projectUser = new ProjectUser(projectUserId, projectUserToken, project, user);
        project.getProjectUsers().add(projectUser);
        projectUserRepository.save(projectUser);
//        User applyUser = userService.getUser(userId);

        // 알림 발송
//        notificationService.send(applyUser, NotificationType.PROJECT_ACCEPT, "신청이 수락되었습니다.", projectId);
    }

    private void validateLeader(String leaderId, Project project) {
        if (!project.getLeaderId().getLeaderId().equals(leaderId)) {
            throw new ApplyBadRequestException("리더가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void updateApplyStatusToReject(String leaderId, String applyUserId, String projectId) {
        Apply apply = applyRepository.findApplyByUserIdAndProjectId(applyUserId, projectId);
        Project project = projectService.findProjectById(projectId);
        // 팀장 검증
        validateLeader(leaderId, project);

        apply.changeStatusToReject();

        // apply 삭제
        applyRepository.delete(apply);

        // 알림 발송
//        User applyUser = userService.getUser(applyUserId);
//        notificationService.send(applyUser, NotificationType.PROJECT_REJECT, "신청이 거절되었습니다.", projectId);
//
//        return RejectApplyResponse.from(apply);
    }

    public GetListApplyResponse getMyApplyList(String userId) {
        User user = userService.getUser(userId);
        List<Apply> applies = applyRepository.findAllProcessByUserId(user.getUserToken());

        return ApplyMapperEntityToDto.applyListToResponse(applies);
    }

    public GetListApplyUserResponse getApplyUserList(String projectId) {
        List<String> userIds = applyRepository.findUserIdsByProjectId(projectId);

        // user_id 는 PK가 아닌 UNIQUE KEY 이므로 findAllById() 사용 못함
        List<User> users = userService.getUsers(userIds);

        return ApplyMapperEntityToDto.userListToResponse(users);
    }

    public Apply getApply(String applyId) {
        return applyRepository.findById(applyId).orElseThrow(() -> new ApplyNotFoundException("신청내역이 존재하지 않습니다."));
    }
}
