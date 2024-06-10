package com.seoultech.synergybe.domain.ticket;

import com.seoultech.synergybe.domain.common.entity.IsDeleted;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.ticket.dto.request.CreateTicketRequest;
import com.seoultech.synergybe.domain.ticket.vo.TicketContent;
import com.seoultech.synergybe.domain.ticket.vo.TicketName;
import com.seoultech.synergybe.domain.ticket.vo.TicketOrderNumber;
import com.seoultech.synergybe.domain.ticket.vo.TicketTagInformation;
import com.seoultech.synergybe.domain.ticketUser.TicketUser;
import com.seoultech.synergybe.domain.common.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.seoultech.synergybe.domain.common.constants.DeletedStatus.IS_DELETED_DEFAULT;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE ticket SET is_deleted = true WHERE ticket_id = ?")
public class Ticket extends BaseTime {
    @Id
    @Column(name = "ticket_id")
    private Long id;

    @Column(name = "ticket_token")
    private String ticketToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Embedded
    private TicketName name;

    @Embedded
    private TicketContent content;

    @Embedded
    private TicketTagInformation information;

    @Embedded
    private TicketOrderNumber orderNumber;

    @OneToMany(mappedBy = "ticket")
    private List<TicketUser> ticketUsers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;

    @Embedded
    private IsDeleted isDeleted = new IsDeleted(IS_DELETED_DEFAULT);

    @Builder
    public Ticket(Long id, String ticketToken, String name, String content, Integer orderNumber, String tag, Project project,
                  String tagColor) {
        this.id = id;
        this.ticketToken = ticketToken;
        this.name = new TicketName(name);
        this.content = new TicketContent(content);
        this.information = new TicketTagInformation(tag, tagColor);
        this.orderNumber = new TicketOrderNumber(orderNumber);
        this.project = project;
        this.status = TicketStatus.BACKLOG;
    }

    public Ticket update(CreateTicketRequest request, TicketStatus status) {
        this.name = new TicketName(request.content());
        this.content = new TicketContent(request.content());
        this.status = status;

        return this;
    }

    public Ticket increaseOrderNum() {
        int value = this.orderNumber.getOrderNumber() + 1;
        this.orderNumber = new TicketOrderNumber(value);

        return this;
    }

    public Ticket decreaseOrderNum() {
        int value = this.orderNumber.getOrderNumber() - 1;
        this.orderNumber = new TicketOrderNumber(value);

        return this;
    }

    public void deleteAssignedUsers() {
        ticketUsers.clear();
    }
}
