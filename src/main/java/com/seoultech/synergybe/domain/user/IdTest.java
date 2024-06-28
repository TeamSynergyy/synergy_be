package com.seoultech.synergybe.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class IdTest {
    @Id
    private Long id;

    private String text;

    @Builder
    public IdTest(Long id, String text) {
        this.id = id;
        this.text = text;
    }
}
