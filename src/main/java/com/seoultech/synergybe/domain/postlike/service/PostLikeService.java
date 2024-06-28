package com.seoultech.synergybe.domain.postlike.service;

import com.seoultech.synergybe.domain.common.constants.LikeStatus;
import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.generator.TokenGenerator;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.infrastructure.PostJpaRepository;
import com.seoultech.synergybe.domain.postlike.PostLike;
import com.seoultech.synergybe.domain.postlike.PostLikeType;
import com.seoultech.synergybe.domain.postlike.dto.response.GetPostLikeResponse;
import com.seoultech.synergybe.domain.postlike.exception.PostLikeNotFoundException;
import com.seoultech.synergybe.domain.postlike.repository.PostLikeRepository;
import com.seoultech.synergybe.domain.user.User;
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
    private final IdGenerator idGenerator;
    private final TokenGenerator tokenGenerator;
    private final PostJpaRepository postJpaRepository;

    @Transactional
    public GetPostLikeResponse updatePostLike(User user, String postToken, PostLikeType type) {
        LikeStatus status;
        log.info("before find");
        Post post = postJpaRepository.findByToken(postToken);
        log.info("after find");

        if (type.getLikeType().equals("like")) {
            status = LikeStatus.LIKE;
        } else {
            status = LikeStatus.UN_LIKE;
        }
        try {
            log.info("updatePostLike update before");
            PostLike updatedPostLike = this.update(user, postToken, status);
            log.info("updatePostLike update after");
            return GetPostLikeResponse.builder().build();
        } catch (Exception e) {
            throw new PostLikeNotFoundException("존재하지 않는 좋아요입니다.");
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
    public synchronized PostLike update(User user, String postToken, LikeStatus likeStatus) {
        Optional<PostLike> postLikeOptional = postLikeRepository.findByUserIdAndPostId(user.getUserToken(), postToken);
        log.info("option");

        Post post = postJpaRepository.findByToken(postToken);
        log.info("post");

        if (postLikeOptional.isPresent()) {
            postLikeOptional.get().updateStatus(likeStatus);

            // like 시 like로 변경, postlike +1
            if (likeStatus == LikeStatus.LIKE) {
                post.getLikes().add(postLikeOptional.get());
            } else if (likeStatus == LikeStatus.UN_LIKE) {
                post.deletePostLike(postLikeOptional.get());
            }

            // unlike 시 unlike로 변경, -1

            return postLikeRepository.saveAndFlush(postLikeOptional.get());
        } else {
            // 없을 경우 생성

            Long postLikeId = idGenerator.generateId();
            String postLikeToken = tokenGenerator.generateToken(IdPrefix.POST_LIKE);

            log.info("updatePostLike builder before");
            PostLike postLike = PostLike.builder()
                    .id(postLikeId)
                    .postLikeToken(postLikeToken)
                    .user(user)
                    .post(post)
                    .build();
            log.info("updatePostLike builder after");
            return postLikeRepository.saveAndFlush(postLike);
        }
    }


    public List<String> findLikedPostIds(User user) {
        return postLikeRepository.findPostIdsByUserId(user.getUserToken());
    }
}
