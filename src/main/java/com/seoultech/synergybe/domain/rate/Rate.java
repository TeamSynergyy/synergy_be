package com.seoultech.synergybe.domain.rate;

import com.seoultech.synergybe.domain.common.entity.IsDeleted;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.rate.vo.RateContent;
import com.seoultech.synergybe.domain.rate.vo.RateScore;
import com.seoultech.synergybe.domain.user.User;
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
@SQLDelete(sql = "UPDATE rate SET is_deleted = true WHERE rate_id = ?")
public class Rate extends BaseTime {
    @Id
    @Column(name = "rate_id")
    private Long id;

    @Column(name = "rate_token")
    private String rateToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "give_user_id")
    private User giveUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id")
    private User receiveUser;

    @Embedded
    private IsDeleted isDeleted = new IsDeleted(IS_DELETED_DEFAULT);

    @Embedded
    private RateScore score;

    @Embedded
    private RateContent content;

    @Builder
    public Rate(Long id, String rateToken, Project project, User giveUser, User receiveUser, int score, String content) {
        this.id = id;
        this.rateToken = rateToken;
        this.project = project;
        this.giveUser = giveUser;
        this.receiveUser = receiveUser;
        this.score = new RateScore(score);
        this.content = new RateContent(content);
    }
}
