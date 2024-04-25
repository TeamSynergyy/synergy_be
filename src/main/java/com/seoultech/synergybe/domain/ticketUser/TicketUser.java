package com.seoultech.synergybe.domain.ticketUser;

import com.seoultech.synergybe.domain.ticket.Ticket;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.common.BaseTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class TicketUser extends BaseTime {
    @Id
    @Column(name = "ticket_user_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public TicketUser(String id, Ticket ticket, User user) {
        this.id = id;
        this.ticket = ticket;
        this.user = user;
    }
}
