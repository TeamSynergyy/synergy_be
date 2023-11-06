package com.seoultech.synergybe.domain.notice;


import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.system.common.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE notice_id = ?")
public class Notice extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "notice_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "is_deleted")
    private boolean isDeleted;


    @Builder
    public Notice(String content, Project project) {
        this.content = content;
        this.project = project;
        this.isDeleted = false;
    }
}
