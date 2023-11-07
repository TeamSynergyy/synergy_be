package com.seoultech.synergybe.domain.rate;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "give_user_id", referencedColumnName = "user_id")
    private User giveUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id", referencedColumnName = "user_id")
    private User receiveUser;

    private Integer score;
    private String content;

    @Builder
    public Rate(Project project, User giveUser, User receiveUser, Integer score, String content) {
        this.project = project;
        this.giveUser = giveUser;
        this.receiveUser = receiveUser;
        this.score = score;
        this.content = content;
    }
}
