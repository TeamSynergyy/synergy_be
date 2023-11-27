package com.seoultech.synergybe.domain.project.controller;

import com.seoultech.synergybe.domain.project.dto.request.CreateProjectRequest;
import com.seoultech.synergybe.domain.project.dto.request.UpdateProjectRequest;
import com.seoultech.synergybe.domain.project.dto.response.ListProjectResponse;
import com.seoultech.synergybe.domain.project.dto.response.ProjectResponse;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody CreateProjectRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(user, request));
    }

    @PutMapping
    public ResponseEntity<ProjectResponse> updateProject(@RequestBody UpdateProjectRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(projectService.updateProject(user, request));
    }

    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<ProjectResponse> deleteProject(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.status(HttpStatus.OK).body(projectService.deleteProject(projectId));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.ok().body(projectService.getProject(projectId));
    }

    @GetMapping("/recent")
    public ResponseEntity<ListProjectResponse> getProjectList(@RequestParam(value = "end", required = false, defaultValue = "9223372036854775807") Long end) {

        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProjectList(end));
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> searchAllProjects(@RequestParam("search") String search, @PageableDefault(size = 7) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(projectService.searchAllProjects(search, pageable));
    }

    @GetMapping(value = "/me/likes")
    public ResponseEntity<ListProjectResponse> getLikedProjects() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(projectService.getLikedProjectList(user));
    }

    @GetMapping(value = "/other")
    public ResponseEntity<ListProjectResponse> getProjectsByUser(@RequestParam("userId") String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProjectListByUser(userId));
    }

    @GetMapping(value = "/recommend")
    public ResponseEntity<ListProjectResponse> getRecommendProjects(@RequestParam(value = "end", required = false, defaultValue = "0") Long end) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(projectService.getRecommendListByUser(user, end));
    }
}
