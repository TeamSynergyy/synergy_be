package com.seoultech.synergybe.domain.project.controller;

import com.seoultech.synergybe.domain.project.dto.request.CreateProjectRequest;
import com.seoultech.synergybe.domain.project.dto.request.UpdateProjectRequest;
import com.seoultech.synergybe.domain.project.dto.response.ProjectResponse;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@RequestBody CreateProjectRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.create("project create", projectService.createProject(user, request)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(@RequestBody UpdateProjectRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("project update", projectService.updateProject(user, request)));
    }

    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> deleteProject(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("project delete", projectService.deleteProject(projectId)));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("project get", projectService.getProject(projectId)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjectList(@Param("end") Long end) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("project list", projectService.getProjectList(end)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> searchAllProjects(@RequestParam("search") String search) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("project search result", projectService.searchAllProjects(search)));
    }
}
