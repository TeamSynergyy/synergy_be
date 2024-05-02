package com.seoultech.synergybe.domain.library.domain;

import com.seoultech.synergybe.domain.library.vo.LibraryLocation;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@Entity
@Getter
@NoArgsConstructor
public class PublicLibrary {
    @Id
    @Column(name = "library_id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "tel_number")
    private String telNumber;

    @Column(name = "homepage_url")
    private String homepageUrl;

    @Column(name = "op_time")
    private String opTime;

    @Column(name = "close_date") // 정기 휴관일
    private String closeDate;

    @Embedded
    private LibraryLocation location;

    @Builder
    public PublicLibrary(
            String name,
            String address,
            String telNumber,
            String homepageUrl,
            String opTime,
            String closeDate,
            Point location
    ) {
        this.name = name;
        this.address = address;
        this.telNumber = telNumber;
        this.homepageUrl = homepageUrl;
        this.opTime = opTime;
        this.closeDate = closeDate;
        this.location = new LibraryLocation(location);
    }
}
