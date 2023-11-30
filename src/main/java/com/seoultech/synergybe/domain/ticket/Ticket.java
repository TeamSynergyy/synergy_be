package com.seoultech.synergybe.domain.ticket;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.ticket.dto.TicketRequest;
import com.seoultech.synergybe.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE ticket SET is_deleted = true WHERE ticket_id = ?")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String title;
    private String tag;
    private LocalDateTime endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public Ticket(String title, String tag, LocalDateTime endAt, User user, Project project) {
        this.title = title;
        this.tag = tag;
        this.endAt = endAt;
        this.user = user;
        this.project = project;
        this.status = TicketStatus.BACKLOG;
    }

    public Ticket update(TicketRequest request, TicketStatus status) {
        this.title = request.getTitle();
        this.tag = request.getTag();
        this.status = status;

        return this;
    }
}
