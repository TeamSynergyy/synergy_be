package com.seoultech.synergybe.domain.postlike;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public PostLike(User user, Post post) {
        this.user = user;
        this.post = post;
        this.status = LikeStatus.LIKE;
    }

    public void updateStatus(LikeStatus status) {
        this.status = status;
    }
}
