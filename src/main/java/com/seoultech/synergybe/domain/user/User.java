package com.seoultech.synergybe.domain.user;

import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;

import com.seoultech.synergybe.domain.user.vo.*;
import com.seoultech.synergybe.domain.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Entity(name = "user")
public class User extends BaseTime {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_token")
    private String userToken;

    @Embedded
    private UserEmail email;

    @Embedded
    private UserPassword password;

    @Embedded
    private UserName name;

    @Embedded
    private UserMajor major;

    @Embedded
    private UserTemperature temperature;

    @Builder
    public User(
            Long id,
            String userToken,
            String email,
            String password,
            String name,
            CustomPasswordEncoder passwordEncoder,
            String major
    ) {
        this.id = id;
        this.userToken = userToken;
        this.email = new UserEmail(email);
        this.password = new UserPassword(password, passwordEncoder);
        this.name = new UserName(name);
        this.major = new UserMajor(major);
        this.temperature = new UserTemperature(36.5);
    }

    public void updateUserInfo(
            String email,
            String name,
            String major
    ) {
        if (userToken == null || email == null || name == null || major == null) throw new NullPointerException();
        this.email = this.email.updateEmail(email);
        this.name = this.name.updateName(name);
        this.major = this.major.updateMajor(major);
    }
}

