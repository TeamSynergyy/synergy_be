package com.seoultech.synergybe.domain.apply;

import com.seoultech.synergybe.domain.common.entity.IsDeleted;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static com.seoultech.synergybe.domain.common.constants.DeletedStatus.IS_DELETED_DEFAULT;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE apply SET is_deleted = true WHERE apply_id = ?")
public class Apply {
    @Id
    @Column(name = "apply_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplyStatus status;

    @Embedded
    private IsDeleted isDeleted = new IsDeleted(IS_DELETED_DEFAULT);

    @Builder
    public Apply(String id, User user, Project project) {
        this.id = id;
        this.user = user;
        this.project = project;
        this.status = ApplyStatus.NEW;
    }

    public void changeStatusToAccept() {
        this.status = ApplyStatus.ACCEPT;
    }

    public void changeStatusToReject() {
        this.status = ApplyStatus.REJECTED;
    }

    public void changeStatusToCompleted() {
        this.status = ApplyStatus.COMPLETED;
    }
}
