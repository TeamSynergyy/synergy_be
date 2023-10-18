package com.seoultech.synergybe.domain.post.service;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.dto.response.PostResponse;
import com.seoultech.synergybe.domain.post.repository.PostRepository;
import com.seoultech.synergybe.domain.user.entity.User;
import com.seoultech.synergybe.system.exception.NotExistPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.seoultech.synergybe.domain.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final UserService userService;

    public PostResponse createPost(User user, CreatePostRequest request) {
        Post savedPost = postRepository.save(request.toEntity(user));

        return PostResponse.from(savedPost);
    }

    public PostResponse updatePost(User user, UpdatePostRequest request) {
        Post post = this.findPostById(request.getPostId());
        Post updatedPost = post.updatePost(request);

        return PostResponse.from(updatedPost);
    }

    public PostResponse deletePost(User user, Long postId) {
        Post post = this.findPostById(postId);
        postRepository.delete(post);

        return PostResponse.from(post);
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(NotExistPostException::new);
    }

    public PostResponse getPost(User user, Long postId) {
        Post post = this.findPostById(postId);

        return PostResponse.from(post);
    }

    public Page<PostResponse> getRecentPostList(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);

        return PostResponse.from(posts);
    }

    public List<PostResponse> getPostList(Long end) {
        List<Post> posts = postRepository.findAllByEndId(end);
        List<PostResponse> postResponses = new ArrayList<>();

        for (Post p : posts) {
            postResponses.add(PostResponse.from(p));
        }

        return postResponses;
    }

    public Page<Post> getFeed(Pageable pageable, User user) {
        List<Long> followingIds = followService.findFollowingIdsByUserId(user.getUserId());

    }
}
