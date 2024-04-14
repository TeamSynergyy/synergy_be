package com.seoultech.synergybe.domain.user;

import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;

import com.seoultech.synergybe.domain.user.vo.*;
import com.seoultech.synergybe.system.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_entity")
public class User extends BaseTime {

    @Id
    @Column(name = "user_id")
    private String userId;

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
            String userId,
            String email,
            String password,
            String name,
            CustomPasswordEncoder passwordEncoder,
            String major
    ) {
        this.userId = userId;
        this.email = new UserEmail(email);
        this.password = new UserPassword(password, passwordEncoder);
        this.name = new UserName(name);
        this.major = new UserMajor(major);
        this.temperature = new UserTemperature(36.5);
    }

    public void updateUserInfo(
            String name,
            String major
    ) {
        this.name = this.name.updateName(name);
        this.major = this.major.updateMajor(major);
    }
}

