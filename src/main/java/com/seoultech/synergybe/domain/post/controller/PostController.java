package com.seoultech.synergybe.domain.post.controller;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.dto.response.GetPostResponse;
import com.seoultech.synergybe.domain.post.service.PostService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "게시글 api")
public class PostController {
    private final PostService postService;

    private final UserService userService;


    @Operation(summary = "post 생성", description = "PostResponse가 반환되며 이미지가 함께 저장됩니다.")
    @PostMapping
    public ResponseEntity<GetPostResponse> createPost(@ModelAttribute CreatePostRequest request, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(user, request));
    }

    @Operation(summary = "post 수정", description = "PostResponse가 반환되며 UpdatePostRequest에 담긴 내용으로 수정됩니다.")
    @PutMapping
    public ResponseEntity<GetPostResponse> updatePost(@RequestBody UpdatePostRequest request, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(request));
    }

    @Operation(summary = "post 삭제", description = "DeletePostResponse가 반환되며, post의 isDelete = true 로 값이 변경됩니다")
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") String postId, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "단건 Post Get", description = "요청된 1개의 Post가 반환됩니다")
    @GetMapping(value = "/{postId}")
    public ResponseEntity<GetPostResponse> getPost(@PathVariable("postId") String postId, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }

    @Operation(summary = "최근 Post 리스트", description = "ListPostResponse가 반환되며, end값(default로는 long 최대값)으로 마지막 조회된 postId를 전달받으며 이후 10개의 post만 반환합니다")
    @GetMapping(value = "/recent")
    public ResponseEntity<ListResponse<GetPostResponse>> getPosts(@Parameter(description = "마지막 조회 Id") @RequestParam(value = "end", required = false, defaultValue = "9223372036854775807") String end) {

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostList(end));
    }

//    @Operation(summary = "팔로워의 Post", description = "팔로워의 Post가 반환됩니다")
//    @GetMapping(value = "/feed")
//    public ResponseEntity<ListPostResponse> getFeed(@Parameter(description = "마지막 조회 Id") @RequestParam(value = "end", required = false, defaultValue = "9223372036854775807") Long end, @LoginUser String userId) {
//        User user = userService.getUser(userId);
//
//        return ResponseEntity.status(HttpStatus.OK).body(postService.getFeed(end, user));
//    }

    @Operation(summary = "검색어를 포함하는 Post", description = "검색어를 포함하는 Post가 반환됩니다")
    @GetMapping
    public ResponseEntity<Page<Post>> searchAllPosts(@Parameter(description = "검색어") @RequestParam("search") String search, @PageableDefault(size = 15) Pageable pageable) {
        log.info(">> keyword : {}", search);

        return ResponseEntity.status(HttpStatus.OK).body(postService.searchAllPosts(search, pageable));
    }

    @Operation(summary = "다른 사용자가 작성한 Post", description = "사용자의 Post가 반환됩니다")
    @GetMapping(value = "/other")
    public ResponseEntity<ListResponse<GetPostResponse>> getPostsByUser(@Parameter(description = "다른 사용자의 Id") @RequestParam("userId") String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostListByUser(userId));
    }


    @Operation(summary = "내가 좋아요한 Post", description = "좋아요한 Post가 반환됩니다")
    @GetMapping(value = "/me/likes")
    public ResponseEntity<ListResponse<GetPostResponse>> getLikedPosts(@LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.getLikedPostList(user));
    }

    @Operation(summary = "추천 Post", description = "나의 활동을 바탕으로 Post가 추천됩니다")
    @GetMapping(value = "/recommend")
    public ResponseEntity<ListResponse<GetPostResponse>> getRecommendPosts(@Parameter(description = "마지막 Post Id") @RequestParam(value = "end", required = false, defaultValue = "0") Long end, @LoginUser String userId) {
        User user = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.getRecommendPostList(user, end));
    }

//    @Operation(summary = "이주의 베스트 Post", description = "좋아요 상위 랭킹 순으로 Post가 반환됩니다")
//    @GetMapping(value = "/week")
//    public ResponseEntity<ListPostResponse> getWeekBestPosts(@LoginUser String userId) {
//
//        return ResponseEntity.status(HttpStatus.OK).body(postService.getWeekBestPostList());
//    }
}
