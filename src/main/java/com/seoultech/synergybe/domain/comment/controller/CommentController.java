package com.seoultech.synergybe.domain.comment.controller;

import com.seoultech.synergybe.domain.comment.dto.request.CommentRequest;
import com.seoultech.synergybe.domain.comment.dto.response.CommentResponse;
import com.seoultech.synergybe.domain.comment.service.CommentService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(user, request));
    }

    @PutMapping
    public ResponseEntity<CommentResponse> updateComment(@RequestBody CommentRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(request));
    }

    @DeleteMapping
    public ResponseEntity<CommentResponse> deleteComment(@RequestBody CommentRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(request));
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentList(@PathVariable("postId") Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentList(postId));
    }
}
