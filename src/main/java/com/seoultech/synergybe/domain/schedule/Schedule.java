package com.seoultech.synergybe.domain.schedule;

import com.seoultech.synergybe.domain.common.entity.IsDeleted;
import com.seoultech.synergybe.domain.project.domain.Project;
import com.seoultech.synergybe.domain.common.BaseTime;
import com.seoultech.synergybe.domain.schedule.vo.ScheduleContent;
import com.seoultech.synergybe.domain.schedule.vo.SchedulePeriod;
import com.seoultech.synergybe.domain.schedule.vo.ScheduleTitle;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import static com.seoultech.synergybe.domain.common.constants.DeletedStatus.IS_DELETED_DEFAULT;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE schedule_id = ?")
public class Schedule extends BaseTime {
    @Id
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_token")
    private String scheduleToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Embedded
    private ScheduleTitle title;

    @Embedded
    private ScheduleContent content;

    private String label;

    @Embedded
    private IsDeleted isDeleted = new IsDeleted(IS_DELETED_DEFAULT);

    @Embedded
    private SchedulePeriod period;

    @Builder
    public Schedule(Long id, String scheduleToken, Project project, String title, String content, String label, LocalDateTime startAt, LocalDateTime endAt) {
        this.id = id;
        this.scheduleToken = scheduleToken;
        this.title = new ScheduleTitle(title);
        this.content = new ScheduleContent(content);
        this.label = label;
        this.project = project;
        this.period = new SchedulePeriod(startAt, endAt);
    }

    public void addProject(Project project) {
        this.project = project;
        project.getSchedules().add(this);
    }
}
