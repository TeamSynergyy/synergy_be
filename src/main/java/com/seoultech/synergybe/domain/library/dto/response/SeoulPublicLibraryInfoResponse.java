package com.seoultech.synergybe.domain.library.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeoulPublicLibraryInfoResponse {
    @JsonProperty("SeoulPublicLibraryInfo")
    private SeoulPublicLibraryInfo seoulPublicLibraryInfo;
}
