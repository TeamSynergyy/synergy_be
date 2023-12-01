package com.seoultech.synergybe.domain.ticketUser;

import com.seoultech.synergybe.domain.ticket.Ticket;
import com.seoultech.synergybe.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class TicketUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Builder
    public TicketUser(Ticket ticket, User user) {
        this.ticket = ticket;
        this.user = user;
    }
}
