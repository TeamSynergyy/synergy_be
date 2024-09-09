package com.seoultech.synergybe.domain.post.controller;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.service.dto.CreatePostDto;
import com.seoultech.synergybe.domain.post.service.dto.UpdatePostDto;
import com.seoultech.synergybe.domain.post.controller.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.controller.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.controller.dto.response.GetPostResponse;
import com.seoultech.synergybe.domain.post.service.PostService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/posts")
@Tag(name = "게시글 api")
public class PostController {
    private final PostService postService;

    @Operation(summary = "post 생성", description = "PostResponse가 반환되며 이미지가 함께 저장됩니다.")
    @PostMapping
    public ResponseEntity<String> createPost(@ModelAttribute @Valid CreatePostRequest request, @LoginUser String userToken) {
        String response = postService.createPost(userToken, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "post 수정", description = "PostResponse가 반환되며 UpdatePostRequest에 담긴 내용으로 수정됩니다.")
    @PutMapping
    public ResponseEntity<String> updatePost(@ModelAttribute @Valid UpdatePostRequest request, @LoginUser String userToken) {
        String response = postService.updatePost(userToken, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "post 삭제", description = "DeletePostResponse가 반환되며, post의 isDelete = true 로 값이 변경됩니다")
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") String postToken, @LoginUser String userToken) {
        postService.deletePost(userToken, postToken);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "단건 Post Get", description = "요청된 1개의 Post가 반환됩니다")
    @GetMapping(value = "/{postId}")
    public ResponseEntity<GetPostResponse> getPost(@PathVariable("postId") String postToken) {
        GetPostResponse response = postService.getPost(postToken);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "최근 Post 리스트", description = "ListPostResponse가 반환되며, end값(default로는 long 최대값)으로 마지막 조회된 postId를 전달받으며 이후 10개의 post만 반환합니다")
    @GetMapping(value = "/recent")
    public ResponseEntity<ListResponse<GetPostResponse>> getPosts(@RequestParam(value = "offsetToken", required = false) String offsetToken) {
        ListResponse<GetPostResponse> response = postService.getRecentPostList(offsetToken);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "팔로워의 Post", description = "팔로워의 Post가 반환됩니다")
    @GetMapping(value = "/feed")
    public ResponseEntity<ListResponse<GetPostResponse>> getFeed(@RequestParam(value = "offsetToken", required = false) String offsetToken, @LoginUser String userToken) {
        ListResponse<GetPostResponse> response = postService.getFeed(offsetToken, userToken);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "검색어를 포함하는 Post", description = "검색어를 포함하는 Post가 반환됩니다")
    @GetMapping
    public ResponseEntity<Page<Post>> searchAllPosts(@Parameter(description = "검색어") @RequestParam("search") String search, @PageableDefault(size = 15) Pageable pageable) {
        Page<Post> response = postService.searchAllPosts(search, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "사용자가 작성한 Post", description = "사용자의 Post가 반환됩니다")
    @GetMapping(value = "/user")
    public ResponseEntity<ListResponse<GetPostResponse>> getPostsByUser(@Parameter(description = "사용자의 userToken") @RequestParam("userToken") String userToken) {
        ListResponse<GetPostResponse> response = postService.getPostListByUser(userToken);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "내가 좋아요한 Post", description = "좋아요한 Post가 반환됩니다")
    @GetMapping(value = "/me/likes")
    public ResponseEntity<ListResponse<GetPostResponse>> getLikedPosts(@LoginUser String userToken) {
        ListResponse<GetPostResponse> response = postService.getMyLikedPostList(userToken);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "이주의 베스트 Post", description = "좋아요 상위 랭킹 순으로 Post가 반환됩니다")
    @GetMapping(value = "/week")
    public ResponseEntity<ListResponse<GetPostResponse>> getWeekBestPosts() {
        ListResponse<GetPostResponse> weekBestPostList = postService.getWeekBestPostList();

        return ResponseEntity.status(HttpStatus.OK).body(weekBestPostList);
    }
}
