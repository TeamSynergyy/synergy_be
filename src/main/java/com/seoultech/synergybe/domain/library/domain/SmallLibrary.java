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
public class SmallLibrary {
    @Id
    @Column(name = "library_id")
    private String id;

    private String name;

    private String address;

    private String telNumber;

    private String homepageUrl;

    private String opTime;

    @Column(name = "close_date") // 정기 휴관일
    private String closeDate;

    @Embedded
    private LibraryLocation location;

    @Builder
    public SmallLibrary(
            String id,
            String name,
            String address,
            String telNumber,
            String homepageUrl,
            String opTime,
            String closeDate,
            Point location
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.telNumber = telNumber;
        this.homepageUrl = homepageUrl;
        this.opTime = opTime;
        this.closeDate = closeDate;
        this.location = new LibraryLocation(location);
    }
}
