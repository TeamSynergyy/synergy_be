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


    /**
     * case 1
     * postlike가 없을경우 새로 생성
     *
     * case 2, 3
     * postlike가 있을 경우
     *
     * 2 - status가 unlike이면 unlike 로 변경
     * post에서 해당 postlike 삭제
     * 3 - status가 like이면 like 로 변경
     * post에서 해당 postlike 추가
     */
    public synchronized PostLike update(User user, Long postId, LikeStatus status) {
        Optional<PostLike> postLikeOptional = postLikeRepository.findByUserUserIdAndPostId(user.getUserId(), postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(NotExistPostException::new);

        if (postLikeOptional.isPresent()) {
            postLikeOptional.get().updateStatus(status);

            // like 시 like로 변경, postlike +1
            if (status == LikeStatus.LIKE) {
                post.getLikes().add(postLikeOptional.get());
            } else if (status == LikeStatus.UNLIKE) {
                post.deletePostLike(postLikeOptional.get());
            }

            // unlike 시 unlike로 변경, -1

            return postLikeRepository.saveAndFlush(postLikeOptional.get());
        } else {
            // 없을 경우 생성
            // postService 호출시 순환참조 발생

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
