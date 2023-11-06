package com.seoultech.synergybe.domain.projectlike.service;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.repository.ProjectRepository;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.projectlike.LikeStatus;
import com.seoultech.synergybe.domain.projectlike.ProjectLike;
import com.seoultech.synergybe.domain.projectlike.ProjectLikeType;
import com.seoultech.synergybe.domain.projectlike.dto.response.ProjectLikeResponse;
import com.seoultech.synergybe.domain.projectlike.repository.ProjectLikeRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.exception.NotExistProjectException;
import com.seoultech.synergybe.system.exception.NotExistProjectLikeException;
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


    @Transactional
    public ProjectLikeResponse updateProjectLike(User user, Long projectId, ProjectLikeType type) {
        LikeStatus status;
        if (type.getLikeType().equals("like")) {
            status = LikeStatus.LIKE;
        } else {
            status = LikeStatus.UNLIKE;
        }
        try {
            ProjectLike updatedProjectLike = this.update(user, projectId, status);

            return ProjectLikeResponse.from(updatedProjectLike);
        } catch (Exception e) {
            throw new NotExistProjectLikeException();
        }
    }

    public synchronized ProjectLike update(User user, Long projectId, LikeStatus status) {
        Optional<ProjectLike> projectLikeOptional = projectLikeRepository.findByUserUserIdAndProjectId(user.getUserId(), projectId);

        if (projectLikeOptional.isPresent()) {
            projectLikeOptional.get().updateStatus(status);

            return projectLikeOptional.get();
        } else {
//            Project project = projectService.findProjectById(projectId);
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(NotExistProjectException::new);
            ProjectLike projectLike = ProjectLike.builder()
                    .user(user)
                    .project(project)
                    .build();

            return projectLikeRepository.saveAndFlush(projectLike);
        }
    }

    public List<Long> findLikedProjectIds(User user) {
        return projectLikeRepository.findProjectIdsByUserId(user.getUserId());
    }
}
