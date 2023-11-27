package com.seoultech.synergybe.domain.apply.service;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.dto.response.AcceptApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ListApplyUserResponse;
import com.seoultech.synergybe.domain.apply.dto.response.RejectApplyResponse;
import com.seoultech.synergybe.domain.apply.repository.ApplyRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;

    private final ProjectService projectService;
    private final ProjectUserRepository projectUserRepository;
    private final UserService userService;

    public ApplyResponse createApply(User user, Long projectId) {
        Project project = projectService.findProjectById(projectId);

        Apply apply = Apply.builder()
                .user(user).project(project).build();
        Apply savedApply = applyRepository.save(apply);

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
        log.info(">> userId {}",userId);
        Optional<Apply> applyOptional = applyRepository.findByUserIdAndProjectId(userId, projectId);

        if (applyOptional.isPresent()) {
            Apply acceptedApply = applyOptional.get();
            acceptedApply.acceptedApplyStatus();
            applyRepository.save(acceptedApply);
            Project project = projectService.findProjectById(projectId);
            User user = userService.getUser(userId);

            ProjectUser projectUser = new ProjectUser(project, user);
            project.getProjectUsers().add(projectUser);
            projectUserRepository.save(projectUser);

            return AcceptApplyResponse.from(applyOptional.get());
        } else {
            throw new NotExistApplyException();
        }
    }

    public RejectApplyResponse rejectApply(String userId, Long projectId) {
        Optional<Apply> applyOptional = applyRepository.findByUserIdAndProjectId(userId, projectId);

        if (applyOptional.isPresent()) {
            applyOptional.get().acceptedApplyStatus();

            return RejectApplyResponse.from(applyOptional.get());
        } else {
            throw new NotExistApplyException();
        }
    }

    public List<ApplyResponse> getMyApplyList(User user) {
        List<Apply> applies = applyRepository.findAllByUserId(user.getUserId());

        return ApplyResponse.from(applies);

    }

    public ListApplyUserResponse getApplyUserList(Long projectId) {
        List<String> userIds = applyRepository.findUserIdsByProjectId(projectId);

        // user_id 는 PK가 아닌 UNIQUE KEY 이므로 findAllById() 사용 못함
        List<User> users = userService.getUsers(userIds);

        return ListApplyUserResponse.from(users);
    }

    public List<Long> getProjectIdsByUserId(String userId) {
        return applyRepository.findProjectIdsByUserId(userId);
    }
}
