package com.seoultech.synergybe.domain.project.interfaces;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.project.domain.Project;
import com.seoultech.synergybe.domain.project.domain.service.ProjectService;
import com.seoultech.synergybe.domain.project.interfaces.dto.request.CreateProjectRequest;
import com.seoultech.synergybe.domain.project.interfaces.dto.request.UpdateProjectRequest;
import com.seoultech.synergybe.domain.project.interfaces.dto.response.GetListProjectResponse;
import com.seoultech.synergybe.domain.project.interfaces.dto.response.GetProjectResponse;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "프로젝트 api")
public class ProjectApiController {
    private final ProjectService projectService;
    private final UserService userService;

    @Operation(summary = "프로젝트 생성", description = "프로젝트가 생성됩니다.")
    @PostMapping
    public ResponseEntity<GetProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request, @LoginUser String userToken) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(userToken, request));
    }

    @Operation(summary = "프로젝트 수정", description = "요청된 정보에 따라 프로젝트가 수정됩니다.")
    @PutMapping
    public ResponseEntity<Void> updateProject(@Valid @RequestBody UpdateProjectRequest request, @LoginUser String userId) {
        projectService.updateProject(userId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프로젝트 삭제", description = "프로젝트가 삭제됩니다.")
    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable("projectId") String projectId, @LoginUser String userId) {
        projectService.deleteProject(userId, projectId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프로젝트 조회", description = "프로젝트를 단건 조회합니다.")
    @GetMapping("/{projectId}")
    public ResponseEntity<GetProjectResponse> getProject(@PathVariable("projectId") String projectId) {

        return ResponseEntity.ok().body(projectService.getProject(projectId));
    }

    @Operation(summary = "프로젝트 목록", description = "최근 프로젝트들의 목록을 반환하며, end값(default로는 long 최대값)으로 마지막 조회된 프로젝트Id를 전달받으며 이후 10개의 post만 반환합니다")
    @GetMapping("/recent")
    public ResponseEntity<GetListProjectResponse> getProjectList(@RequestParam(value = "offset", required = false) Long offset) {

        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProjectList(offset));
    }

    @Operation(summary = "검색어를 포함하는 프로젝트", description = "검색어를 포함하는 프로젝트가 반환됩니다")
    @GetMapping
    public ResponseEntity<Page<Project>> searchAllProjects(@RequestParam("search") String search, @PageableDefault(size = 7) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(projectService.searchAllProjects(search, pageable));
    }

    @Operation(summary = "내가 좋아요한 프로젝트", description = "좋아요한 프로젝트가 반환됩니다")
    @GetMapping(value = "/me/likes")
    public ResponseEntity<ListResponse<GetProjectResponse>> getLikedProjects(@LoginUser String userId) {
        User user = userService.getUserByToken(userId);

        return ResponseEntity.status(HttpStatus.OK).body(projectService.getLikedProjectList(user));
    }

    @Operation(summary = "다른 사용자가 작성한 프로젝트", description = "사용자의 프로젝트가 반환됩니다")
    @GetMapping(value = "/other")
    public ResponseEntity<ListResponse<String>> getProjectsByUser(@RequestParam("userId") String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProjectListByUser(userId));
    }

    @Operation(summary = "추천 프로젝트", description = "나의 활동을 바탕으로 프로젝트가 추천됩니다")
    @GetMapping(value = "/recommend")
    public ResponseEntity<ListResponse<GetProjectResponse>> getRecommendProjects(@RequestParam(value = "end", required = false, defaultValue = "0") Long end, @LoginUser String userId) {
        User user = userService.getUserByToken(userId);

        return ResponseEntity.status(HttpStatus.OK).body(projectService.getRecommendListByUser(user, end));
    }
}
