package com.seoultech.synergybe.domain.projectlike.service;

import com.seoultech.synergybe.domain.common.constants.LikeStatus;
import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.generator.TokenGenerator;
import com.seoultech.synergybe.domain.project.domain.Project;
import com.seoultech.synergybe.domain.project.infrastructure.ProjectRepository;
import com.seoultech.synergybe.domain.projectlike.ProjectLike;
import com.seoultech.synergybe.domain.projectlike.ProjectLikeType;
import com.seoultech.synergybe.domain.projectlike.dto.response.ProjectLikeResponse;
import com.seoultech.synergybe.domain.projectlike.exception.ProjectLikeNotFoundException;
import com.seoultech.synergybe.domain.projectlike.repository.ProjectLikeRepository;
import com.seoultech.synergybe.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectLikeService {
    private final ProjectLikeRepository projectLikeRepository;
    private final ProjectRepository projectRepository;
    private final IdGenerator idGenerator;
    private final TokenGenerator tokenGenerator;


    @Transactional
    public ProjectLikeResponse updateProjectLike(User user, String projectToken, ProjectLikeType type) {
        LikeStatus status;
        if (type.getLikeType().equals("like")) {
            status = LikeStatus.LIKE;
        } else {
            status = LikeStatus.UN_LIKE;
        }
        try {
            ProjectLike updatedProjectLike = this.update(user, projectToken, status);

//            return ProjectLikeResponse.from(updatedProjectLike);
            return ProjectLikeResponse.builder().build();
        } catch (Exception e) {
            throw new ProjectLikeNotFoundException("존재하지 않는 프로젝트 좋아요입니다.");
        }
    }

    public synchronized ProjectLike update(User user, String projectId, LikeStatus status) {
        Optional<ProjectLike> projectLikeOptional = projectLikeRepository.findByUserUserIdAndProjectId(user.getUserToken(), projectId);

        if (projectLikeOptional.isPresent()) {
            projectLikeOptional.get().updateStatus(status);

            return projectLikeOptional.get();
        } else {
            Long projectLikeId = idGenerator.generateId();
            String projectLikeToken = tokenGenerator.generateToken(IdPrefix.PROJECT_LIKE);
//            Project project = projectService.findProjectById(projectId);
            Project project = projectRepository.findById(projectId)
                    .orElseThrow();
            ProjectLike projectLike = ProjectLike.builder()
                    .id(projectLikeId)
                    .projectLikeToken(projectLikeToken)
                    .user(user)
                    .project(project)
                    .build();

            return projectLikeRepository.saveAndFlush(projectLike);
        }
    }

    public List<String> findLikedProjectIds(User user) {
        return projectLikeRepository.findProjectIdsByUserId(user.getUserToken());
    }
}
