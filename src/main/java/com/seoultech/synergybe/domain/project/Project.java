package com.seoultech.synergybe.domain.project;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.common.entity.IsDeleted;
import com.seoultech.synergybe.domain.notice.Notice;
import com.seoultech.synergybe.domain.project.dto.request.UpdateProjectRequest;
import com.seoultech.synergybe.domain.project.vo.ProjectLeaderId;
import com.seoultech.synergybe.domain.project.vo.ProjectPeriod;
import com.seoultech.synergybe.domain.project.vo.ProjectLocation;
import com.seoultech.synergybe.domain.projectlike.ProjectLike;
import com.seoultech.synergybe.domain.projectuser.ProjectUser;
import com.seoultech.synergybe.domain.schedule.Schedule;
import com.seoultech.synergybe.domain.common.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.geo.Point;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.seoultech.synergybe.domain.common.constants.DeletedStatus.IS_DELETED_DEFAULT;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE project_id = ?")
public class Project extends BaseTime {
    @Id
    @Column(name = "project_id")
    private String id;

    private String name;

    private String content;

    @Enumerated(EnumType.STRING)
    private ProjectField field;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Embedded
    private ProjectLeaderId leaderId;

    @Embedded
    private ProjectPeriod period;

    @Embedded
    private ProjectLocation location;

    @OneToMany(mappedBy = "project")
    private List<Notice> notices = new ArrayList<>();

    @Embedded
    private IsDeleted isDeleted = new IsDeleted(IS_DELETED_DEFAULT);

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

    @OneToMany(mappedBy = "project")
    private List<Schedule> schedules = new ArrayList<>();

    @Builder
    public Project(String id, String name, String content, ProjectField field, Point location, LocalDateTime startAt,
                   LocalDateTime endAt, String leaderId) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.field = field;
        this.location = new ProjectLocation(location);
        this.status = ProjectStatus.READY;
        this.leaderId = new ProjectLeaderId(leaderId);
        this.period = new ProjectPeriod(startAt, endAt, leaderId);
    }

    public Project updateProject(UpdateProjectRequest request) {
        this.name = request.getName();
        this.content = request.getContent();
        this.field = request.getField();

        return this;
    }

    public void updateProjectLeaderId(String leaderId) {
        this.leaderId = new ProjectLeaderId(leaderId);
    }
}
