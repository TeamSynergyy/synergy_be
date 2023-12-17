package com.seoultech.synergybe.domain.postlike.controller;

import com.seoultech.synergybe.domain.postlike.PostLikeType;
import com.seoultech.synergybe.domain.postlike.dto.response.PostLikeResponse;
import com.seoultech.synergybe.domain.postlike.service.PostLikeService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "게시글 좋아요 api")
public class PostLikeController {
    private final PostLikeService postLikeService;

    private final UserService userService;

    @Operation(summary = "좋아요 신청, 취소", description = "좋아요를 신청 및 취소하며 좋아요 타입에 따라 좋아요 상태가 변화하므로 PUT 메서드 하나로 관리됩니다.")
    @PutMapping(value = "/{postId}/like")
    public ResponseEntity<PostLikeResponse> updatePostLike(@PathVariable("postId") Long postId, @RequestBody PostLikeType type, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postLikeService.updatePostLike(user, postId, type));
    }

}
