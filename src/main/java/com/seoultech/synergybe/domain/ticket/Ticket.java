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
    private String tagColor;
    private Double assignedTime;
    private Integer orderNumber;
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
    public Ticket(String title, Integer orderNumber, String tag, LocalDateTime endAt, User user, Project project, TicketStatus status,
                  String tagColor, Double assignedTime) {
        this.title = title;
        this.tag = tag;
        this.endAt = endAt;
        this.user = user;
        this.orderNumber = orderNumber;
        this.project = project;
        this.status = status;
        this.tagColor = tagColor;
        this.assignedTime = assignedTime;
        this.isDeleted = false;
    }

    public Ticket update(TicketRequest request, TicketStatus status) {
        this.title = request.getTitle();
        this.tag = request.getTag();
        this.orderNumber = request.getOrderNumber();
        this.status = status;

        return this;
    }

    public Ticket increaseOrderNum() {
        this.orderNumber += 1;

        return this;
    }
}
