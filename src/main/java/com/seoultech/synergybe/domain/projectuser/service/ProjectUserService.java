package com.seoultech.synergybe.domain.projectuser.service;

import com.seoultech.synergybe.domain.common.idgenerator.IdGenerator;
import com.seoultech.synergybe.domain.common.idgenerator.IdPrefix;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.repository.ProjectRepository;
import com.seoultech.synergybe.domain.projectuser.ProjectUser;
import com.seoultech.synergybe.domain.projectuser.exception.ProjectUserNotFoundException;
import com.seoultech.synergybe.domain.projectuser.repository.ProjectUserRepository;
import com.seoultech.synergybe.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectUserService {

    private final ProjectUserRepository projectUserRepository;
    private final IdGenerator idGenerator;
    private final ProjectRepository projectRepository;

    public void createProjectUser(Project project, User user) {
        Optional<ProjectUser> projectUserOptional = projectUserRepository.findByProjectIdAndUserUserId(project.getId(), user.getUserId());

        if (projectUserOptional.isPresent()) {
            // 이미 생성됨
        } else {
            String projectUserId = idGenerator.generateId(IdPrefix.PROJECT_USER);
            ProjectUser projectUser = ProjectUser.builder()
                    .id(projectUserId).project(project).user(user)
                    .build();
            project.getProjectUsers().add(projectUser);
            projectUserRepository.save(projectUser);
        }
    }

    public List<String> getProjectUserIds(Long projectId) {
        return projectUserRepository.findProjectUserIdsByProjectId(projectId);
    }

    public void deleteProjectUser(Project project, User user) {
        Optional<ProjectUser> projectUserOptional = projectUserRepository.findByProjectIdAndUserUserId(project.getId(), user.getUserId());

        if (projectUserOptional.isPresent()) {
            projectUserRepository.delete(projectUserOptional.get());
        } else {
            throw new ProjectUserNotFoundException("존재하지 않는 프로젝트 유저입니다.");
        }
    }

    public List<Long> getProjectIdsByUserId(String userId) {
        return projectUserRepository.findProjectIdsByUserId(userId);
    }
}
