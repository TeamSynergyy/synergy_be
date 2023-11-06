package com.seoultech.synergybe.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class Name {

    private String originalFilename;
    private String storeFileName;
}
