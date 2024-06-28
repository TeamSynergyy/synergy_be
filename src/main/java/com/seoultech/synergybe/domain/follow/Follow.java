package com.seoultech.synergybe.domain.follow;

import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;



@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Follow extends BaseTime {
    @Id
    @Column(name = "follow_id")
    private Long id;

    @Column(name = "follow_token")
    private String followToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FollowStatus status;

    @Builder
    public Follow(Long id, String followToken, User follower, User following) {
        this.id = id;
        this.followToken = followToken;
        this.follower = follower;
        this.following = following;
        this.status = FollowStatus.FOLLOW;
    }

    public void updateStatus(FollowStatus status) {
        this.status = status;
    }
}
