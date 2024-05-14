package com.seoultech.synergybe.domain.post.service;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.dto.response.GetListPostResponse;
import com.seoultech.synergybe.domain.post.dto.response.GetPostResponse;
import com.seoultech.synergybe.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PostService {
    GetPostResponse createPost(String userId, CreatePostRequest request);

    GetPostResponse updatePost(String userId, UpdatePostRequest request);

    void deletePost(String userId, String postId);

    List<Post> findAllByFollowingIdAndEndId(String userId, Long end);

    GetListPostResponse getPostRecentList(Long offset);

    ListResponse<GetPostResponse> getPostListByUser(String userId);

    ListResponse<GetPostResponse> getFeed(Long end, User user);

    Page<Post> searchAllPosts(String keyword, Pageable pageable);

    Specification<Post> search(String keyword);

    ListResponse<GetPostResponse> getRecommendPostList(User user, Long end);

    GetPostResponse getPost(String postId);

    Post findPostById(String postId);

    GetListPostResponse getWeekBestPostList();
}
