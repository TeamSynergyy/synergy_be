package com.seoultech.synergybe.domain.notice;


import com.seoultech.synergybe.domain.common.entity.IsDeleted;
import com.seoultech.synergybe.domain.notice.vo.NoticeContent;
import com.seoultech.synergybe.domain.notice.vo.NoticeTitle;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.common.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

import static com.seoultech.synergybe.domain.common.constants.DeletedStatus.IS_DELETED_DEFAULT;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE notice_id = ?")
public class Notice extends BaseTime {
    @Id
    @Column(name = "notice_id")
    private String id;

    @Embedded
    private NoticeTitle title;

    @Embedded
    private NoticeContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Embedded
    private IsDeleted isDeleted = new IsDeleted(IS_DELETED_DEFAULT);


    @Builder
    public Notice(String id, String title, String content, Project project) {
        this.id = id;
        this.title = new NoticeTitle(title);
        this.content = new NoticeContent(content);
        this.project = project;
    }
}
