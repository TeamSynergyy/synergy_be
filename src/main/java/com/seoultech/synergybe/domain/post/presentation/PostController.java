package com.seoultech.synergybe.domain.post.presentation;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.post.business.dto.CreatePostDto;
import com.seoultech.synergybe.domain.post.business.dto.UpdatePostDto;
import com.seoultech.synergybe.domain.post.presentation.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.presentation.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.presentation.dto.response.GetPostResponse;
import com.seoultech.synergybe.domain.post.business.PostService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<GetPostResponse> createPost(@ModelAttribute @Valid CreatePostRequest request, @LoginUser String userId) {
        // service layer에 맞게 변경 필요
        CreatePostDto createPostDto = new CreatePostDto(
                request.title(),
                request.content(),
                request.files() == null ? List.of() : request.files()
        );
        GetPostResponse response = postService.createPost(userId, createPostDto);


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "post 수정", description = "PostResponse가 반환되며 UpdatePostRequest에 담긴 내용으로 수정됩니다.")
    @PutMapping
    public ResponseEntity<GetPostResponse> updatePost(@ModelAttribute @Valid UpdatePostRequest request, @LoginUser String userId) {
        UpdatePostDto updatePostDto = new UpdatePostDto(
                request.postId(),
                request.title(),
                request.content(),
                request.files() == null ? List.of() : request.files()
        );
        GetPostResponse response = postService.updatePost(userId, updatePostDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "post 삭제", description = "DeletePostResponse가 반환되며, post의 isDelete = true 로 값이 변경됩니다")
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") String postId, @LoginUser String userId) {
        postService.deletePost(userId, postId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "단건 Post Get", description = "요청된 1개의 Post가 반환됩니다")
    @GetMapping(value = "/{postId}")
    public ResponseEntity<GetPostResponse> getPost(@PathVariable("postId") String postId) {
        GetPostResponse response = postService.getPost(postId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "최근 Post 리스트", description = "ListPostResponse가 반환되며, end값(default로는 long 최대값)으로 마지막 조회된 postId를 전달받으며 이후 10개의 post만 반환합니다")
    @GetMapping(value = "/recent")
    public ResponseEntity<ListResponse<GetPostResponse>> getPosts(@Parameter(description = "offset") @RequestParam(value = "offset", required = false, defaultValue = "9223372036854775807") Long offset) {
        ListResponse<GetPostResponse> response = postService.getRecentList(offset);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "팔로워의 Post", description = "팔로워의 Post가 반환됩니다")
    @GetMapping(value = "/feed")
    public ResponseEntity<ListResponse<GetPostResponse>> getFeed(@Parameter(description = "마지막 조회 Id") @RequestParam(value = "end", required = false, defaultValue = "9223372036854775807") Long end, @LoginUser String userId) {
        ListResponse<GetPostResponse> response = postService.getFeed(end, userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @Operation(summary = "검색어를 포함하는 Post", description = "검색어를 포함하는 Post가 반환됩니다")
//    @GetMapping
//    public ResponseEntity<Page<Post>> searchAllPosts(@Parameter(description = "검색어") @RequestParam("search") String search, @PageableDefault(size = 15) Pageable pageable) {
//        log.info(">> keyword : {}", search);
//
//        return ResponseEntity.status(HttpStatus.OK).body(postService.searchAllPosts(search, pageable));
//    }

    @Operation(summary = "사용자가 작성한 Post", description = "사용자의 Post가 반환됩니다")
    @GetMapping(value = "/user")
    public ResponseEntity<ListResponse<GetPostResponse>> getPostsByUser(@Parameter(description = "사용자의 Id") @RequestParam("userId") String userId) {
        ListResponse<GetPostResponse> list = postService.getList(userId);

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }


//    @Operation(summary = "내가 좋아요한 Post", description = "좋아요한 Post가 반환됩니다")
//    @GetMapping(value = "/me/likes")
//    public ResponseEntity<> getLikedPosts(@LoginUser String userId) {
//
//        return ResponseEntity.status(HttpStatus.OK).body(postService.getLikedPostList(userId));
//    }

    @Operation(summary = "이주의 베스트 Post", description = "좋아요 상위 랭킹 순으로 Post가 반환됩니다")
    @GetMapping(value = "/week")
    public ResponseEntity<ListResponse<GetPostResponse>> getWeekBestPosts() {
        ListResponse<GetPostResponse> weekBestPostList = postService.getWeekBestPostList();

        return ResponseEntity.status(HttpStatus.OK).body(weekBestPostList);
    }
}
