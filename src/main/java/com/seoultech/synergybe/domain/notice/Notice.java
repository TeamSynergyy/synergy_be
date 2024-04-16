package com.seoultech.synergybe.domain.notice;


import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.common.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE notice_id = ?")
public class Notice extends BaseTime {
    @Id
    @Column(name = "notice_id")
    private String id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "is_deleted")
    private boolean isDeleted;


    @Builder
    public Notice(String id, String content, Project project) {
        this.id = id;
        this.content = content;
        this.project = project;
        this.isDeleted = false;
    }
}
