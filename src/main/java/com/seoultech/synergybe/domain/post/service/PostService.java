package com.seoultech.synergybe.domain.post.service;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.controller.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.controller.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.controller.dto.response.GetPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    String createPost(String userToken, CreatePostRequest request);

    String updatePost(String userToken, UpdatePostRequest request);

    void deletePost(String userToken, String postToken);

    GetPostResponse getPost(String postToken);

    ListResponse<GetPostResponse> getRecentPostList(String offsetToken);

    ListResponse<GetPostResponse> getFeed(String offsetToken, String userToken);

    Page<Post> searchAllPosts(String keyword, Pageable pageable);

    ListResponse<GetPostResponse> getPostListByUser(String userToken);

    ListResponse<GetPostResponse> getMyLikedPostList(String userToken);

    ListResponse<GetPostResponse> getWeekBestPostList();
}
