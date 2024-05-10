package com.seoultech.synergybe.domain.library.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SeoulSmallLibraryInfo {
    @JsonProperty("list_total_count")
    private int listTotalCount;

    @JsonProperty("RESULT")
    private SeoulPublicLibraryInfo.Result RESULT;

    @JsonProperty("row")
    private List<RawLibrary> row;

    @Getter
    @NoArgsConstructor
    public static class Result {
        @JsonProperty("CODE")
        private String CODE;

        @JsonProperty("MESSAGE")
        private String MESSAGE;
    }

    @Getter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class RawLibrary {
        @JsonProperty("LBRRY_SEQ_NO")
        private String LBRRY_SEQ_NO;

        @JsonProperty("LBRRY_NAME")
        private String LBRRY_NAME;

        @JsonProperty("GU_CODE")
        private String GU_CODE;

        @JsonProperty("CODE_VALUE")
        private String CODE_VALUE;

        @JsonProperty("ADRES")
        private String ADRES;

        @JsonProperty("TEL_NO")
        private String TEL_NO;

        @JsonProperty("HMPG_URL")
        private String HMPG_URL;

        @JsonProperty("OP_TIME")
        private String OP_TIME;

        @JsonProperty("FDRM_CLOSE_DATE")
        private String FDRM_CLOSE_DATE;

        @JsonProperty("LBRRY_SE_NAME")
        private String LBRRY_SE_NAME;

        @JsonProperty("XCNTS")
        private String XCNTS;

        @JsonProperty("YDNTS")
        private String YDNTS;
    }
}
