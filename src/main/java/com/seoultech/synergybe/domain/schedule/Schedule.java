package com.seoultech.synergybe.domain.schedule;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.schedule.dto.request.ScheduleRequest;
import com.seoultech.synergybe.system.common.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE schedule_id = ?")
public class Schedule extends BaseTime {
    @Id
    @Column(name = "schedule_id")
    private String id;

    private String title;

    private String content;

    private String label;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Schedule(String id, Project project, String title, String content, String label, LocalDateTime startAt, LocalDateTime endAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.label = label;
        this.project = project;
        this.startAt = startAt;
        this.endAt = endAt;
        this.isDeleted = false;
    }

    public void addProject(Project project) {
        this.project = project;
        project.getSchedules().add(this);
    }

    public Schedule updateSchedule(ScheduleRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.startAt = request.getStartAt();
        this.endAt = request.getEndAt();

        return this;
    }
}
