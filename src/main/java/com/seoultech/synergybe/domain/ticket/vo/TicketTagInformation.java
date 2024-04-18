package com.seoultech.synergybe.domain.ticket.vo;

import com.seoultech.synergybe.domain.ticket.exception.TicketBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketTagInformation {
    @Column(name = "tag")
    private String tag;

    @Column(name = "tag_color")
    private String tagColor;

    public TicketTagInformation(String tag, String tagColor) {
        this.tag = tag;
        this.tagColor = tagColor;
    }
}
