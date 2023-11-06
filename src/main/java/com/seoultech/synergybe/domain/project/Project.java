package com.seoultech.synergybe.domain.project;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.project.dto.request.UpdateProjectRequest;
import com.seoultech.synergybe.domain.projectlike.ProjectLike;
import com.seoultech.synergybe.domain.projectuser.ProjectUser;
import com.seoultech.synergybe.system.common.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE post_id = ?")
public class Project extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    private String name;

    private String content;

    @Enumerated(EnumType.STRING)
    private ProjectField field;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String leaderId;

    private Point location;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(
            mappedBy = "project"
    )
    private List<ProjectLike> likes = new ArrayList<>();

    @OneToMany(
            mappedBy = "project"
    )
    private List<Apply> applies = new ArrayList<>();

    @OneToMany(
            mappedBy = "project"
    )
    private List<ProjectUser> projectUsers = new ArrayList<>();

    @Builder
    public Project(String name, String content, ProjectField field, Point location, LocalDateTime startAt,
                   LocalDateTime endAt, String leaderId) {
        this.name = name;
        this.content = content;
        this.field = field;
        this.location = location;
        this.status = ProjectStatus.READY;
        this.startAt = startAt;
        this.endAt = endAt;
        this.leaderId = leaderId;
        this.isDeleted = false;
    }

    public Project updateProject(UpdateProjectRequest request) {
        this.name = request.getName();
        this.content = request.getContent();
        this.field = request.getField();
        this.location = request.getLocation();
        this.startAt = request.getStartAt();
        this.endAt = request.getEndAt();

        return this;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }
}
