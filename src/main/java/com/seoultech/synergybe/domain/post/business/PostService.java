package com.seoultech.synergybe.domain.post.business;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.follow.implement.FollowReader;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.business.dto.UpdatePostDto;
import com.seoultech.synergybe.domain.post.implement.*;
import com.seoultech.synergybe.domain.post.business.dto.CreatePostDto;
import com.seoultech.synergybe.domain.post.presentation.dto.response.GetPostResponse;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.repository.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserReader userReader;
    private final FollowReader followReader;
    private final PostReader postReader;
    private final PostFactory postFactory;
    private final PostManager postManager;
    private final PostValidator postValidator;

    public GetPostResponse createPost(String userId, CreatePostDto postData) {
        User user = userReader.read(userId);
        Post post = postFactory.createPost(user, postData.title(), postData.content(), postData.files());
        postManager.save(post);

        return GetPostResponse.builder()
                .postId(post.getId())
                .build();
    }

    public GetPostResponse updatePost(String userId, UpdatePostDto updatePostDto) {
        User user = userReader.read(userId);
        Post post = postReader.read(updatePostDto.postId());

        postValidator.validateUser(user, post);
        postManager.update(post, updatePostDto);

        return GetPostResponse.builder()
                .postId(post.getId())
                .build();
    }

    public void deletePost(String userId, String postId) {
        User user = userReader.read(userId);
        Post post = postReader.read(postId);

        postValidator.validateUser(user, post);
        postManager.delete(post);
    }

    public ListResponse<GetPostResponse> getRecentList(Long offset) {

        return postReader.readRecentList(offset);
    }

    public GetPostResponse getPost(String postId) {
        Post post = postReader.read(postId);
        System.out.println(postId);

        return PostMapperEntityToDto.postToResponse(post);
    }

    public ListResponse<GetPostResponse> getFeed(Long end, String userId) {
        List<String> followingIds = followReader.readFollowingIds(userId);
        System.out.println(followingIds.size());

        return postReader.readFeed(followingIds);
    }

    public ListResponse<GetPostResponse> getList(String userId) {
        return postReader.readListByUser(userId);
    }

//    Page<Post> searchAllPosts(String keyword, Pageable pageable);
//
//    Specification<Post> search(String keyword);

    public ListResponse<GetPostResponse> getWeekBestPostList() {
        return postReader.readWeekBest();
    }
}
