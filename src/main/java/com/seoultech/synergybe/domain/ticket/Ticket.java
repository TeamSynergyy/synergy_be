package com.seoultech.synergybe.domain.ticket;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.ticket.dto.TicketRequest;
import com.seoultech.synergybe.domain.ticketUser.TicketUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE ticket SET is_deleted = true WHERE ticket_id = ?")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;
    private String tag;
    private String tagColor;
    private Double assignedTime;
    private Integer orderNumber;
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "ticket")
    private List<TicketUser> ticketUsers = new ArrayList<>();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public Ticket(String title, String content, Integer orderNumber, String tag, LocalDateTime endAt, Project project, TicketStatus status,
                  String tagColor, Double assignedTime) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.endAt = endAt;
        this.orderNumber = orderNumber;
        this.project = project;
        this.status = status;
        this.tagColor = tagColor;
        this.assignedTime = assignedTime;
        this.isDeleted = false;
    }

    public Ticket update(TicketRequest request, TicketStatus status) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.tag = request.getTag();
        this.orderNumber = request.getOrderNumber();
        this.status = status;

        return this;
    }

    public Ticket increaseOrderNum() {
        this.orderNumber += 1;

        return this;
    }

    public Ticket decreaseOrderNum() {
        this.orderNumber -=1;

        return this;
    }
}
