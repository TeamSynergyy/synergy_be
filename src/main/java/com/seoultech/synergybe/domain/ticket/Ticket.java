package com.seoultech.synergybe.domain.ticket;

import com.seoultech.synergybe.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String tag;
    private LocalDateTime endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Builder
    public Ticket(String title, String tag, LocalDateTime endAt, User user) {
        this.title = title;
        this.tag = tag;
        this.endAt = endAt;
        this.user = user;
    }
}
