package com.seoultech.synergybe.domain.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Library {
    @Id
    @Column(name = "library_id")
    private String id;
}
