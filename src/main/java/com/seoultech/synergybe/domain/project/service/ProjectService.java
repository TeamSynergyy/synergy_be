package com.seoultech.synergybe.domain.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.apply.repository.ApplyRepository;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.dto.request.CreateProjectRequest;
import com.seoultech.synergybe.domain.project.dto.request.UpdateProjectRequest;
import com.seoultech.synergybe.domain.project.dto.response.ListProjectResponse;
import com.seoultech.synergybe.domain.project.dto.response.ProjectResponse;
import com.seoultech.synergybe.domain.project.repository.ProjectRepository;
import com.seoultech.synergybe.domain.projectlike.service.ProjectLikeService;
import com.seoultech.synergybe.domain.projectuser.service.ProjectUserService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.exception.NotExistProjectException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    private final ProjectUserService projectUserService;

    private final ProjectLikeService projectLikeService;

    private final ApplyRepository applyRepository;
    private final UserService userService;

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

    public List<User> getUserListByProject(Long projectId) {
        List<String> userIds = projectUserService.getProjectUserIds(projectId);
        List<User> userList = new ArrayList<>();
        for (String userId : userIds) {
            userList.add(userService.getUser(userId));
        }

        return userList;
    }


    // todo
    // 현재 user가 참여하고 있는 projectList가 필요함
    // 현재 진행중인 프로젝트만 찾아야 함
    public ListProjectResponse getProjectListByUser(String userId) {
        List<Long> projectIds = projectUserService.getProjectIdsByUserId(userId);

        return ListProjectResponse.from(ProjectResponse.from(projectRepository.findAllById(projectIds)));
    }

    public ListProjectResponse getRecommendListByUser(User user, Long end) {

        try {
            log.info("get recommend project list start");
            String userId = user.getUserId();
            log.info("user Id {}", userId);

            RestTemplate restTemplate = new RestTemplate();
            log.info("rest template new");
            String fastApiUrl = "http://fastapi:8000"; // 컨테이너 이름과 포트
            String response = restTemplate.getForObject(fastApiUrl + "/recommend/projects/" + userId, String.class);

            log.info("Response from FastAPI: {}", response);

            List<Long> projectIds = this.extractIds(response);

            if (projectIds.isEmpty()) {
                List<Project> projects = new ArrayList<>();
                return ListProjectResponse.from(ProjectResponse.fromEmpty(projects));
            }

            // end 기준 end ~ end + 10 순서에 있는 게시글 가져오기
            int startIdx = end.intValue();
            int endIdx = Math.min(startIdx + 10, projectIds.size());

            List<Long> result = projectIds.subList(startIdx, endIdx);

            List<Project> projects = projectRepository.findAllById(result);


            log.info("Response from FastAPI: {}", response);
            return ListProjectResponse.from(ProjectResponse.from(projects));
        } catch (Exception e) {
            log.error(">> 추천 프르젝트 가져오기 실패 {}", e.getMessage());
            throw new NotExistProjectException();
        }
    }

    private List<Long> extractIds(String response) {
        try {
            // 받은 JSON 응답을 자바 리스트로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            log.error(">> http cliend response body {}", response);

            return objectMapper.readValue(response, new TypeReference<List<Long>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
