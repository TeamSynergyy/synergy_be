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
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", referencedColumnName = "user_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", referencedColumnName = "user_id")
    private User following;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FollowStatus status;

    @Builder
    public Follow(String id, User follower, User following) {
        this.id = id;
        this.follower = follower;
        this.following = following;
        this.status = FollowStatus.FOLLOW;
    }

    public void updateStatus(FollowStatus status) {
        this.status = status;
    }
}
