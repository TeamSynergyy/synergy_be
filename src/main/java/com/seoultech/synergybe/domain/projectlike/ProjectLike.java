package com.seoultech.synergybe.domain.projectlike;


import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.ProjectStatus;
import com.seoultech.synergybe.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class ProjectLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Builder
    public ProjectLike(User user, Project project) {
        this.user = user;
        this.project = project;
        this.status = ProjectStatus.READY;
    }

    public void updateStatus(ProjectStatus status) {
        this.status = status;
    }
}
