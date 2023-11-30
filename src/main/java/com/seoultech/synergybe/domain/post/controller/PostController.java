package com.seoultech.synergybe.domain.post.controller;

import com.seoultech.synergybe.domain.post.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.dto.response.DeletePostResponse;
import com.seoultech.synergybe.domain.post.dto.response.ListPostResponse;
import com.seoultech.synergybe.domain.post.dto.response.PostResponse;
import com.seoultech.synergybe.domain.post.service.PostService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/posts")
@Tag(name = "게시글 api", description = "게시글 관련 api 입니다")
public class PostController {
    private final PostService postService;

    private final UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "2023-10-18";
    }


    @Operation(summary = "post 생성", description = "PostResponse가 반환되며 이미지가 함께 저장됩니다.")
    @PostMapping
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PostResponse> createPost(@ModelAttribute CreatePostRequest request, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(user, request));
    }

    @Operation(summary = "post 수정", description = "PostResponse가 반환되며 UpdatePostRequest에 담긴 내용으로 수정됩니다.")
    @PutMapping
    public ResponseEntity<PostResponse> updatePost(@RequestBody UpdatePostRequest request, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(request));
    }

    @Operation(summary = "post 삭제", description = "DeletePostResponse가 반환되며, post의 isDelete = true 로 값이 변경됩니다")
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<DeletePostResponse> deletePost(@PathVariable("postId") Long postId, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postId));
    }

    @Operation(summary = "단건 Post Get", description = "요청된 1개의 Post가 반환됩니다")
    @GetMapping(value = "/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("postId") Long postId, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(user, postId));
    }

    @Operation(summary = "최근 Post 리스트", description = "ListPostResponse가 반환되며, end값으로 마지막 조회된 post의 개수를 전달받으며 이후 10개의 post만 반환합니다")
    @GetMapping(value = "/recent")
    public ResponseEntity<ListPostResponse> getPosts(@RequestParam(value = "end", required = false, defaultValue = "9223372036854775807") Long end) {

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostList(end));
    }

    @Operation(summary = "Feed Post Get", description = "요청된 1개의 Post가 반환됩니다")
    @GetMapping(value = "/feed")
    public ResponseEntity<ListPostResponse> getFeed(@RequestParam(value = "end", required = false, defaultValue = "9223372036854775807") Long end, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.getFeed(end, user));
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> searchAllPosts(@RequestParam("search") String search, @PageableDefault(size = 15) Pageable pageable) {
        log.info(">> keyword : {}", search);

        return ResponseEntity.status(HttpStatus.OK).body(postService.searchAllPosts(search, pageable));
    }

    @GetMapping(value = "/other")
    public ResponseEntity<ListPostResponse> getPostsByUser(@RequestParam("userId") String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostListByUser(userId));
    }


    @GetMapping(value = "/me/likes")
    public ResponseEntity<ListPostResponse> getLikedPosts(@LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.getLikedPostList(user));
    }

    @GetMapping(value = "/recommend")
    public ResponseEntity<ListPostResponse> getRecommendPosts(@RequestParam(value = "end", required = false, defaultValue = "0") Long end, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.getRecommendPostList(user, end));
    }

    @GetMapping(value = "/week")
    public ResponseEntity<ListPostResponse> getWeekBestPosts(@LoginUser String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(postService.getWeekBestPostList());
    }
}
