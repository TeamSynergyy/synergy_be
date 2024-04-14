package com.seoultech.synergybe.domain.rate;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.user.User;
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
@SQLDelete(sql = "UPDATE rate SET is_deleted = true WHERE rate_id = ?")
public class Rate {
    @Id
    @Column(name = "rate_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "give_user_id", referencedColumnName = "user_id")
    private User giveUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id", referencedColumnName = "user_id")
    private User receiveUser;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    private Integer score;
    private String content;

    @Builder
    public Rate(String id, Project project, User giveUser, User receiveUser, Integer score, String content) {
        this.id = id;
        this.project = project;
        this.giveUser = giveUser;
        this.receiveUser = receiveUser;
        this.score = score;
        this.content = content;
        this.isDeleted = false;
    }
}
