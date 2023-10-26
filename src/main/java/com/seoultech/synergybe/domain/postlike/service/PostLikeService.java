package com.seoultech.synergybe.domain.postlike.service;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.repository.PostRepository;
import com.seoultech.synergybe.domain.post.service.PostService;
import com.seoultech.synergybe.domain.postlike.LikeStatus;
import com.seoultech.synergybe.domain.postlike.PostLike;
import com.seoultech.synergybe.domain.postlike.PostLikeType;
import com.seoultech.synergybe.domain.postlike.dto.response.PostLikeResponse;
import com.seoultech.synergybe.domain.postlike.repository.PostLikeRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.exception.NotExistPostException;
import com.seoultech.synergybe.system.exception.NotExistPostLikeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;

    private final PostRepository postRepository;

    @Transactional
    public PostLikeResponse updatePostLike(User user, Long postId, PostLikeType type) {
        LikeStatus status;
        if (type.getLikeType().equals("like")) {
            status = LikeStatus.LIKE;
        } else {
            status = LikeStatus.UNLIKE;
        }
        try {
            log.info("updatePostLike update before");
            PostLike updatedPostLike = this.update(user, postId, status);
            log.info("updatePostLike update after");
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
            // postService 호출시 순환참조 발생
//            Post post = postService.findPostById(postId);
            Post post = postRepository.findById(postId)
                    .orElseThrow(NotExistPostException::new);
            log.info("updatePostLike builder before");
            PostLike postLike = PostLike.builder()
                    .user(user)
                    .post(post)
                    .build();
            log.info("updatePostLike builder after");
            return postLikeRepository.saveAndFlush(postLike);
        }
    }


    public List<Long> findLikedPostIds(User user) {
        return postLikeRepository.findPostIdsByUserId(user.getUserId());
    }
}
