package com.seoultech.synergybe.domain.postlike;

import com.seoultech.synergybe.domain.common.constants.LikeStatus;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.common.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class PostLike extends BaseTime {
    @Id
    @Column(name = "post_like_id")
    private Long id;

    @Column(name = "post_like_token")
    private String postLikeToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "like_status", nullable = false)
    private LikeStatus likeStatus = LikeStatus.LIKE;

    @Builder
    public PostLike(Long id, String postLikeToken, User user, Post post) {
        this.id = id;
        this.postLikeToken = postLikeToken;
        this.user = user;
        this.post = post;
        post.getLikes().add(this);
    }

    public void updateStatus(LikeStatus likeStatus) {
        this.likeStatus = likeStatus;
    }
}
