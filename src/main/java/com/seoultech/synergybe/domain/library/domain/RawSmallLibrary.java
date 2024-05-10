package com.seoultech.synergybe.domain.library.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "raw_small_library")
@Getter
@NoArgsConstructor
public class RawSmallLibrary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "library_seq") // 도서관 일련번호
    private String smallLibrarySeq;

    @Column(name = "name") // 도서관 명
    private String name;

    @Column(name = "gu_code") // 구 코드
    private String guCode;

    @Column(name = "gu_code_value") // 구 명
    private String guCodeValue;

    @Column(name = "address") // 주소
    private String address;

    @Column(name = "tel_number") // 전화번호
    private String telNumber;

    @Column(name = "hompage_url") // 홈페이지 url
    private String hompageUrl;

    @Column(name = "op_time") // 운영시간
    private String opTime;

    @Column(name = "close_date") // 정기 휴관일
    private String closeDate;

    @Column(name = "se_name") // 도서관 구분명
    private String seName;

    @Column(name = "latitude") // 위도
    private Double latitude;

    @Column(name = "longitude") // 경도
    private Double longitude;

    @Builder
    public RawSmallLibrary(
            String smallLibrarySeq,
            String name,
            String guCode,
            String guCodeValue,
            String address,
            String telNumber,
            String hompageUrl,
            String opTime,
            String closeDate,
            String seName,
            Double latitude,
            Double longitude
    ) {
        this.smallLibrarySeq = smallLibrarySeq;
        this.name = name;
        this.guCode = guCode;
        this.guCodeValue = guCodeValue;
        this.address = address;
        this.telNumber = telNumber;
        this.hompageUrl = hompageUrl;
        this.opTime = opTime;
        this.closeDate = closeDate;
        this.seName = seName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
