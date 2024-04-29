package com.seoultech.synergybe.domain.comment.controller;

import com.seoultech.synergybe.domain.comment.dto.request.CreateCommentRequest;
import com.seoultech.synergybe.domain.comment.dto.request.UpdateCommentRequest;
import com.seoultech.synergybe.domain.comment.dto.response.GetCommentResponse;
import com.seoultech.synergybe.domain.comment.service.CommentService;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 api")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Comment 생성", description = "게시글에 댓글이 생성되며, 사용자와 시간이 포함됩니다.")
    @PostMapping
    public ResponseEntity<GetCommentResponse> createComment(@Valid @RequestBody CreateCommentRequest request, @LoginUser String userId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(userId, request));
    }

    @Operation(summary = "Comment 수정", description = "댓글이 요청에 대해 수정됩니다.")
    @PutMapping
    public ResponseEntity<GetCommentResponse> updateComment(@Valid @RequestBody UpdateCommentRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(request));
    }

    @Operation(summary = "Comment 삭제", description = "댓글이 삭제됩니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Comment 목록", description = "게시글의 댓글 목록을 반환합니다.")
    @GetMapping(value = "/{postId}")
    public ResponseEntity<ListResponse<GetCommentResponse>> getCommentList(@PathVariable("postId") String postId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentList(postId));
    }
}
