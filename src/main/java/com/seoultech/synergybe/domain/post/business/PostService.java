package com.seoultech.synergybe.domain.post.business;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.data.PostRepository;
import com.seoultech.synergybe.domain.post.implement.PostFactory;
import com.seoultech.synergybe.domain.post.implement.PostReader;
import com.seoultech.synergybe.domain.post.implement.PostValidator;
import com.seoultech.synergybe.domain.post.presentation.dto.CreatePostDto;
import com.seoultech.synergybe.domain.post.presentation.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.presentation.dto.response.GetListPostResponse;
import com.seoultech.synergybe.domain.post.presentation.dto.response.GetPostResponse;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.repository.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserReader userReader;
    private final PostReader postReader;
    private final PostFactory postFactory;
    private final PostValidator postValidator;
    private final PostRepository postRepository;

    protected GetPostResponse createPost(String userId, CreatePostDto postData) {
        User user = userReader.read(userId);
        Post post = postFactory.createPost(user, postData.title(), postData.content(), postData.files());
        postRepository.save(post);

        return GetPostResponse.builder()
                .postId(post.getId())
                .build();
    }

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
