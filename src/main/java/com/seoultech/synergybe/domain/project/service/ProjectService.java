package com.seoultech.synergybe.domain.project.service;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.dto.request.CreateProjectRequest;
import com.seoultech.synergybe.domain.project.dto.request.UpdateProjectRequest;
import com.seoultech.synergybe.domain.project.dto.response.ProjectResponse;
import com.seoultech.synergybe.domain.project.repository.ProjectRepository;
import com.seoultech.synergybe.domain.user.entity.User;
import com.seoultech.synergybe.system.exception.NotExistProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectResponse createProject(User user, CreateProjectRequest request) {
        Project savedProject = projectRepository.save(request.toEntity(user));

        return ProjectResponse.from(savedProject);
    }

    public ProjectResponse updateProject(User user, UpdateProjectRequest request) {
        Project project = this.findProjectById(request.getProjectId());
        Project updatedProject = project.updateProject(request);

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

        return ProjectResponse.from(project);
    }

    public List<ProjectResponse> getProjectList(Long end) {
        List<Project> projects = projectRepository.findAllByEndId(end);

        return ProjectResponse.from(projects);
    }

    public List<ProjectResponse> searchAllProjects(String keyword) {

        Specification<Project> spec = this.search(keyword);

        List<Project> projects = projectRepository.findAll(spec);

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
                            cb.like(projectRoot.get("content"), "%" + keyword + "%")
                    );
                } catch (Exception e) {
                    throw new NotExistProjectException();
                }
            }
        };
    }
}
