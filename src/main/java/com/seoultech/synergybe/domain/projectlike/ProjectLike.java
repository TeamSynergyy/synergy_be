package com.seoultech.synergybe.domain.projectlike;


import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class ProjectLike {
    @Id
    @Column(name = "project_like_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public ProjectLike(String id, User user, Project project) {
        this.id = id;
        this.user = user;
        this.project = project;
    }

    public void updateStatus(LikeStatus status) {
        this.status = status;
    }
}
