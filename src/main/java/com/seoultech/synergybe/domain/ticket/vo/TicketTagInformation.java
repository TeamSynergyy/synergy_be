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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketTagInformation that = (TicketTagInformation) o;
        return Objects.equals(tag, that.tag) && Objects.equals(tagColor, that.tagColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, tagColor);
    }
}
