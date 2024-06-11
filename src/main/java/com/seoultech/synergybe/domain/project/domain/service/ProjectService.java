package com.seoultech.synergybe.domain.project.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.generator.TokenGenerator;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.project.domain.Project;
import com.seoultech.synergybe.domain.project.interfaces.dto.request.CreateProjectRequest;
import com.seoultech.synergybe.domain.project.interfaces.dto.request.UpdateProjectRequest;
import com.seoultech.synergybe.domain.project.interfaces.dto.response.GetListProjectResponse;
import com.seoultech.synergybe.domain.project.interfaces.dto.response.GetProjectResponse;
import com.seoultech.synergybe.domain.project.exception.ProjectBadRequestException;
import com.seoultech.synergybe.domain.project.exception.ProjectNotFoundException;
import com.seoultech.synergybe.domain.project.infrastructure.ProjectRepository;
import com.seoultech.synergybe.domain.projectlike.service.ProjectLikeService;
import com.seoultech.synergybe.domain.projectuser.service.ProjectUserService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectUserService projectUserService;
    private final ProjectLikeService projectLikeService;
    private final IdGenerator idGenerator;
    private final TokenGenerator tokenGenerator;
    private final UserService userService;

    @Transactional
    public GetProjectResponse createProject(String userId, CreateProjectRequest request) {
        User user = userService.getUser(userId);
        Long projectId = idGenerator.generateId();
        String projectToken = tokenGenerator.generateToken(IdPrefix.POST);
        Point point = new Point(request.longitude(), request.latitude());
        Project project = Project.builder()
                .id(projectId)
                .projectToken(projectToken)
                .name(request.name())
                .content(request.content())
                .field(request.field())
                .location(point)
                .startAt(request.startAt())
                .endAt(request.endAt())
                .leaderId(user.getId())
                .build();
        Project savedProject = projectRepository.save(project);
        projectUserService.createProjectUser(savedProject, user);
        return GetProjectResponse.builder()
                .projectToken(savedProject.getProjectToken())
                .build();
    }

    private void validateProjectUser(String userToken, String projectId) {
        Project project = findProjectById(projectId);
        List<String> userListTokens = project.getProjectUsers().stream().map(projectUser -> projectUser.getUser().getUserToken()).toList();

        if (!userListTokens.contains(userToken)) {
            throw new ProjectBadRequestException("프로젝트 변경 권한이 없습니다.");
        }
    }

    private void validateProjectLeader(String leaderId, String projectId) {
        Project project = findProjectById(projectId);

        if (!project.getLeaderId().equals(leaderId)) {
            throw new ProjectBadRequestException("프로젝트 변경 권한이 없습니다.");
        }
    }

    @Transactional
    public void updateProject(String userToken, UpdateProjectRequest request) {
        // todo
        // 프로젝트 멤버 검증
        validateProjectUser(userToken, request.projectId());

        Project project = this.findProjectById(request.projectId());
        Project updatedProject = project.updateProject(request);
        projectRepository.save(updatedProject);
    }

    public GetProjectResponse deleteProject(String userId, String projectId) {
        // todo
        // 프로젝트 리더 검증
        validateProjectLeader(userId, projectId);

        Project project = this.findProjectById(projectId);
        projectRepository.delete(project);

        return GetProjectResponse.builder().build();
    }

    public Project findProjectById(String projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
    }
    public GetProjectResponse getProject(String projectToken) {
        Project project = this.findProjectById(projectToken);

        return GetProjectResponse.builder()
                .projectToken(projectToken)
                .name(project.getName().getName())
                .content(project.getContent().getContent())
                .field(project.getField().name())
                .location(project.getLocation().getLocation())
                .startAt(project.getPeriod().getStartAt())
                .endAt(project.getPeriod().getEndAt())
                .leaderId(String.valueOf(project.getLeaderId().getLeaderId()))
                .status(project.getStatus().getName())
                .teamUserIds(project.getProjectUsers().stream().map(projectUser -> projectUser.getUser().getUserToken()).collect(Collectors.toList()))
                .build();
    }

    public GetListProjectResponse getProjectList(Long offset) {
        List<Project> projects = projectRepository.findAllByCreateAtAndLimit(offset);
        int totalCount = projectRepository.countTotalProjectSize();

        boolean hasNext;
        int pageSize = 10;

        if (totalCount > pageSize + offset) {
            hasNext = true;
        } else {
            hasNext = false;
        }

        return ProjectMapperEntityToDto.projectListToResponse(projects, hasNext);

    }

    public Page<Project> searchAllProjects(String keyword, Pageable pageable) {

        Specification<Project> spec = this.search(keyword);

        Page<Project> projects = projectRepository.findAll(spec, pageable);

        return projects;
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
                    throw new ProjectNotFoundException("존재하지 않는 프로젝트입니다.");
                }
            }
        };
    }

    public ListResponse<GetProjectResponse> getLikedProjectList(User user) {
        List<String> projectIds = projectLikeService.findLikedProjectIds(user);
        List<Project> projects = projectRepository.findAllById(projectIds);

        return new ListResponse(projects);
    }

    public List<User> getUserListByProject(String projectId) {
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
    public ListResponse<String> getProjectListByUser(String userId) {
        List<String> projectIds = projectUserService.getProjectIdsByUserId(userId);

        return new ListResponse(projectIds);
    }

    public ListResponse<GetProjectResponse> getRecommendListByUser(User user, Long end) {

        try {
            log.info("get recommend project list start");
            String userToken = user.getUserToken();
            log.info("user Id {}", userToken);

            RestTemplate restTemplate = new RestTemplate();
            log.info("rest template new");
            String fastApiUrl = "http://fastapi:8000"; // 컨테이너 이름과 포트
            String response = restTemplate.getForObject(fastApiUrl + "/recommend/projects/" + userToken, String.class);

            log.info("Response from FastAPI: {}", response);

            List<String> projectIds = this.extractIds(response);

            if (projectIds.isEmpty()) {
                List<Project> projects = new ArrayList<>();
//                return ListProjectResponse.from(GetProjectResponse.fromEmpty(projects));
                return new ListResponse(projects);
            }

            // end 기준 end ~ end + 10 순서에 있는 게시글 가져오기
            int startIdx = end.intValue();
            int endIdx = Math.min(startIdx + 10, projectIds.size());

            List<String> result = projectIds.subList(startIdx, endIdx);

            List<Project> projects = projectRepository.findAllById(result);


            log.info("Response from FastAPI: {}", response);
//            return ListProjectResponse.from(GetProjectResponse.from(projects));
            return new ListResponse(projects);
        } catch (Exception e) {
            log.error(">> 추천 프르젝트 가져오기 실패 {}", e.getMessage());
            throw new ProjectNotFoundException("존재하지 않는 프로젝트입니다.");
        }
    }

    private List<String> extractIds(String response) {
        try {
            // 받은 JSON 응답을 자바 리스트로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            log.error(">> http cliend response body {}", response);

            return objectMapper.readValue(response, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
