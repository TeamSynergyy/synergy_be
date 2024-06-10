package com.seoultech.synergybe.domain.projectlike;


import com.seoultech.synergybe.domain.common.constants.LikeStatus;
import com.seoultech.synergybe.domain.project.Project;
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
public class ProjectLike extends BaseTime {
    @Id
    @Column(name = "project_like_id")
    private Long id;

    @Column(name = "project_like_token")
    private String projectLikeToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "like_status", nullable = false)
    private LikeStatus likeStatus = LikeStatus.LIKE;



    @Builder
    public ProjectLike(Long id, String projectLikeToken, User user, Project project) {
        this.id = id;
        this.projectLikeToken = projectLikeToken;
        this.user = user;
        this.project = project;
        project.getLikes().add(this);
    }

    public void updateStatus(LikeStatus likeStatus) {
        this.likeStatus = likeStatus;

    }
}
