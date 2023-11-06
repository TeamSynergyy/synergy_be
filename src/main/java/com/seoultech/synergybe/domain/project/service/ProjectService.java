package com.seoultech.synergybe.domain.project.service;

import com.seoultech.synergybe.domain.apply.repository.ApplyRepository;
import com.seoultech.synergybe.domain.apply.service.ApplyService;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.dto.request.CreateProjectRequest;
import com.seoultech.synergybe.domain.project.dto.request.UpdateProjectRequest;
import com.seoultech.synergybe.domain.project.dto.response.ListProjectResponse;
import com.seoultech.synergybe.domain.project.dto.response.ProjectResponse;
import com.seoultech.synergybe.domain.project.repository.ProjectRepository;
import com.seoultech.synergybe.domain.projectlike.service.ProjectLikeService;
import com.seoultech.synergybe.domain.projectuser.service.ProjectUserService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.exception.NotExistProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    private final ProjectUserService projectUserService;

    private final ProjectLikeService projectLikeService;

    private final ApplyRepository applyRepository;

    public ProjectResponse createProject(User user, CreateProjectRequest request) {
        Project savedProject = projectRepository.save(request.toEntity(user));
        projectUserService.createProjectUser(savedProject, user);


        return ProjectResponse.from(savedProject);
    }

    public ProjectResponse updateProject(User user, UpdateProjectRequest request) {
        Project project = this.findProjectById(request.getProjectId());
        Project updatedProject = project.updateProject(request);
        projectRepository.save(updatedProject);

        return ProjectResponse.from(updatedProject);
    }

    public ProjectResponse deleteProject(Long projectId) {
        Project project = this.findProjectById(projectId);


        projectRepository.delete(project);

        return ProjectResponse.from(project);
    }

    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(NotExistProjectException::new);
    }

    public ProjectResponse getProject(Long projectId) {
        Project project = this.findProjectById(projectId);
        List<String> projectUserIds = projectUserService.getProjectUserIds(project);

        return ProjectResponse.from(project);
    }

    public ListProjectResponse getProjectList(Long end) {
        List<Project> projects = projectRepository.findAllByEndId(end);

        return ListProjectResponse.from(ProjectResponse.from(projects));
    }

    public Page<ProjectResponse> searchAllProjects(String keyword, Pageable pageable) {

        Specification<Project> spec = this.search(keyword);

        Page<Project> projects = projectRepository.findAll(spec, pageable);

        return ProjectResponse.from(projects);
    }

    public Specification<Project> search(String keyword) {
        return new Specification<Project>() {
            @Override
            public Predicate toPredicate(Root<Project> projectRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);

                try {
                    return cb.or(
                            cb.like(projectRoot.get("name"), "%" + keyword + "%"),
                            cb.like(projectRoot.get("content"), "%" + keyword + "%"),
                            cb.like(projectRoot.get("field").as(String.class),"%" + keyword + "%")
                    );
                } catch (Exception e) {
                    throw new NotExistProjectException();
                }
            }
        };
    }

    public ListProjectResponse getLikedProjectList(User user) {
        List<Long> projectIds = projectLikeService.findLikedProjectIds(user);
        List<Project> projects = projectRepository.findAllById(projectIds);

        return ListProjectResponse.from(ProjectResponse.from(projects));
    }

    // todo
    // 현재 user가 참여하고 있는 projectList가 필요함
    // leaderId로 찾을 수 있고
    // projectMember를 통해서 project를 찾을 수 있음
    // 현재 진행중인 프로젝트만 찾아야 함
    public ListProjectResponse getProjectListByUser(String userId) {

        List<Long> projectIds = applyRepository.findProjectIdsByUserId(userId);

        List<Project> projectsByUserId = projectRepository.findAllById(projectIds);

        List<Project> projectsByLeader = projectRepository.findAllByLeaderId(userId);

        List<Project> combinedProjects = Stream.concat(projectsByUserId.stream(), projectsByLeader.stream())
                .collect(Collectors.toList());

        return ListProjectResponse.from(ProjectResponse.from(combinedProjects));
    }
}
