package com.seoultech.synergybe.domain.projectuser.service;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.projectuser.ProjectUser;
import com.seoultech.synergybe.domain.projectuser.repository.ProjectUserRepository;
import com.seoultech.synergybe.domain.user.entity.User;
import com.seoultech.synergybe.system.exception.NotExistProjectUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectUserService {

    private final ProjectUserRepository projectUserRepository;

    public void createProjectUser(Project project, User user) {
        Optional<ProjectUser> projectUserOptional = projectUserRepository.findByProjectIdAndUserUserId(project.getId(), user.getUserId());

        if (projectUserOptional.isPresent()) {
            // 이미 생성됨
        } else {
            project.setLeaderId(user.getUserId());

            ProjectUser projectUser = new ProjectUser(project, user);
            project.getProjectUsers().add(projectUser);
            projectUserRepository.save(projectUser);
        }
    }

    public void deleteProjectUser(Project project, User user) {
        Optional<ProjectUser> projectUserOptional = projectUserRepository.findByProjectIdAndUserUserId(project.getId(), user.getUserId());

        if (projectUserOptional.isPresent()) {
            projectUserRepository.delete(projectUserOptional.get());
        } else {
            throw new NotExistProjectUserException();
        }

    }
}
