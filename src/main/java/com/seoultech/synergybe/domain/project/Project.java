package com.seoultech.synergybe.domain.project;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.common.entity.IsDeleted;
import com.seoultech.synergybe.domain.notice.Notice;
import com.seoultech.synergybe.domain.project.dto.request.UpdateProjectRequest;
import com.seoultech.synergybe.domain.project.vo.*;
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

//    @Id
//    @Column(name = "project_sequence")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long seq;

    @Embedded
    private ProjectName name;

    @Embedded
    private ProjectContent content;

    @Enumerated(EnumType.STRING)
    @Column(name = "field")
    private ProjectField field;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
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
        this.name = new ProjectName(name);
        this.content = new ProjectContent(content);
        this.field = field;
        this.location = new ProjectLocation(location);
        this.status = ProjectStatus.NEW;
        this.leaderId = new ProjectLeaderId(leaderId);
        this.period = new ProjectPeriod(startAt, endAt, leaderId);
    }

    public Project updateProject(UpdateProjectRequest request) {

        return this;
    }

    public void updateProjectLeaderId(String leaderId) {
        this.leaderId = new ProjectLeaderId(leaderId);
    }

    public void changeStatusToNew() {
        this.status = ProjectStatus.NEW;
    }

    public void changeStatusToRecruitment() {
        this.status = ProjectStatus.RECRUITMENT;
    }

    public void changeStatusToInProgress() {
        this.status = ProjectStatus.IN_PROGRESS;
    }

    public void changeStatusToCompleted() {
        this.status = ProjectStatus.COMPLETED;
    }

}
