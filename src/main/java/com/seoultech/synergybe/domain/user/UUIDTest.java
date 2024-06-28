package com.seoultech.synergybe.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UUIDTest {
    @Id
    private String id;

    private String text;

    @Builder
    public UUIDTest(String id, String text) {
        this.id = id;
        this.text = text;
    }
}
