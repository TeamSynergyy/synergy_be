package com.seoultech.synergybe.domain.post.controller;

import com.seoultech.synergybe.domain.post.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.dto.response.PostResponse;
import com.seoultech.synergybe.domain.post.service.PostService;
import com.seoultech.synergybe.domain.user.entity.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/posts")
public class PostController {
    private final PostService postService;

    private final UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "2023-10-18";
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody CreatePostRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.create("post", postService.createPost(user, request)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@RequestBody UpdatePostRequest request) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("post", postService.updatePost(user, request)));
    }

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> deletePost(@PathVariable("postId") Long postId) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("post", postService.deletePost(user, postId)));
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable("postId") Long postId) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("post", postService.getPost(user, postId)));
    }

    @GetMapping(value = "/recent")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getRecentPostList(@PageableDefault(size = 10, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("post recent list", postService.getRecentPostList(pageable)));
    }

    @GetMapping(value = "/list")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getPostList(@Param("end") Long end) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("post list", postService.getPostList(end)));
    }

    @GetMapping(value = "/feed")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getFeed(@Param("end") Long end) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("post feed list", postService.getFeed(end, user)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> searchAllPosts(@RequestParam("search") String search) {
        log.info(">> keyword : {}", search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("search Post list", postService.searchAllPosts(search)));
    }




}
