package com.seoultech.synergybe.domain.follow;

import com.seoultech.synergybe.domain.user.User;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

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
    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
        this.status = FollowStatus.FOLLOW;
    }

    public void updateStatus(FollowStatus status) {
        this.status = status;
    }
}
