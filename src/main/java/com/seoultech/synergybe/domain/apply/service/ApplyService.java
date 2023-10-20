package com.seoultech.synergybe.domain.apply.service;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.dto.response.AcceptApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.ListApplyUserResponse;
import com.seoultech.synergybe.domain.apply.dto.response.RejectApplyResponse;
import com.seoultech.synergybe.domain.apply.repository.ApplyRepository;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.exception.NotExistApplyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;

    private final ProjectService projectService;
    private final UserService userService;

    public ApplyResponse createApply(User user, Long projectId) {
        Project project = projectService.findProjectById(projectId);

        Apply apply = Apply.builder()
                .user(user).project(project).build();
        Apply savedApply = applyRepository.save(apply);

        return ApplyResponse.from(savedApply);
    }

    public ApplyResponse deleteApply(User user, Long projectId) {
        Optional<Apply> applyOptional = applyRepository.findByUserUserIdAndProjectId(user.getUserId(), projectId);

        if (applyOptional.isPresent()) {
            applyRepository.delete(applyOptional.get());

            return ApplyResponse.from(applyOptional.get());
        } else {
            throw new NotExistApplyException();
        }
    }

    public AcceptApplyResponse acceptApply(String userId, Long projectId) {
        Optional<Apply> applyOptional = applyRepository.findByUserUserIdAndProjectId(userId, projectId);

        if (applyOptional.isPresent()) {
            applyOptional.get().acceptedApplyStatus();

            return AcceptApplyResponse.from(applyOptional.get());
        } else {
            throw new NotExistApplyException();
        }
    }

    public RejectApplyResponse rejectApply(String userId, Long projectId) {
        Optional<Apply> applyOptional = applyRepository.findByUserUserIdAndProjectId(userId, projectId);

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
        String userIdsString = String.join(",", userIds);

        // user_id 는 PK가 아닌 UNIQUE KEY 이므로 findAllById() 사용 못함
        List<User> users = userService.getUsers(userIdsString);

        return ListApplyUserResponse.from(users);
    }
}
