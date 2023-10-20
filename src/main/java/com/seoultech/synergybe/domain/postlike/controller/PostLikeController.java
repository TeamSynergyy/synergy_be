package com.seoultech.synergybe.domain.postlike.controller;

import com.seoultech.synergybe.domain.post.service.PostService;
import com.seoultech.synergybe.domain.postlike.PostLikeType;
import com.seoultech.synergybe.domain.postlike.dto.response.PostLikeResponse;
import com.seoultech.synergybe.domain.postlike.service.PostLikeService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/posts")
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;

    private final UserService userService;

    private final PostService postService;

    @PutMapping(value = "/{postId}/like")
    public ResponseEntity<ApiResponse<PostLikeResponse>> updatePostLike(@PathVariable("postId") Long postId, @RequestBody PostLikeType type) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("post like update", postLikeService.updatePostLike(user, postId, type)));
    }

}
