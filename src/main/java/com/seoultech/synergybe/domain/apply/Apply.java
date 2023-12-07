package com.seoultech.synergybe.domain.apply;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE apply SET is_deleted = true WHERE apply_id = ?")
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private ApplyStatus status;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public Apply(User user, Project project) {
        this.user = user;
        this.project = project;
        this.status = ApplyStatus.PROCESS;
        this.isDeleted = false;
    }

    public void accepted() {
        this.status = ApplyStatus.COMPLETED;
    }

    public void rejected() {
        this.status = ApplyStatus.REJECTED;
    }
}
