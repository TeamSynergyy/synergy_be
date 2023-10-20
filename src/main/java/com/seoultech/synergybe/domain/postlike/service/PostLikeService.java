package com.seoultech.synergybe.domain.postlike.service;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.service.PostService;
import com.seoultech.synergybe.domain.postlike.LikeStatus;
import com.seoultech.synergybe.domain.postlike.PostLike;
import com.seoultech.synergybe.domain.postlike.PostLikeType;
import com.seoultech.synergybe.domain.postlike.dto.response.PostLikeResponse;
import com.seoultech.synergybe.domain.postlike.repository.PostLikeRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.exception.NotExistPostLikeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;

    private final PostService postService;

    @Transactional
    public PostLikeResponse updatePostLike(User user, Long postId, PostLikeType type) {
        LikeStatus status;
        if (type.getLikeType().equals("like")) {
            status = LikeStatus.LIKE;
        } else {
            status = LikeStatus.UNLIKE;
        }
        try {
            PostLike updatedPostLike = this.update(user, postId, status);

            return PostLikeResponse.from(updatedPostLike);
        } catch (Exception e) {
            throw new NotExistPostLikeException();
        }
    }

    public synchronized PostLike update(User user, Long postId, LikeStatus status) {
        Optional<PostLike> postLikeOptional = postLikeRepository.findByUserUserIdAndPostId(user.getUserId(), postId);

        if (postLikeOptional.isPresent()) {
            postLikeOptional.get().updateStatus(status);

            return postLikeOptional.get();
        } else {
            Post post = postService.findPostById(postId);
            PostLike postLike = PostLike.builder()
                    .user(user)
                    .post(post)
                    .build();
            return postLikeRepository.saveAndFlush(postLike);
        }

    }
}
