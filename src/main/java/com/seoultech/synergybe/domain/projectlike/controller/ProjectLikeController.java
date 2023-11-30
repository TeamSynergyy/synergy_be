package com.seoultech.synergybe.domain.projectlike.controller;

import com.seoultech.synergybe.domain.projectlike.ProjectLikeType;
import com.seoultech.synergybe.domain.projectlike.dto.response.ProjectLikeResponse;
import com.seoultech.synergybe.domain.projectlike.service.ProjectLikeService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/projects")
@RequiredArgsConstructor
public class ProjectLikeController {
    private final ProjectLikeService projectLikeService;

    private final UserService userService;

    @PutMapping(value = "/{projectId}/like")
    public ResponseEntity<ProjectLikeResponse> updateProjectLike(@PathVariable("projectId") Long projectId, @RequestBody ProjectLikeType type, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(projectLikeService.updateProjectLike(user, projectId, type));
    }
}
