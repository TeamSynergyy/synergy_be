package com.seoultech.synergybe.domain.user;

import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;

import com.seoultech.synergybe.domain.user.vo.*;
import com.seoultech.synergybe.system.common.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

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

    public User(
            String email,
            String password,
            String name,
            CustomPasswordEncoder passwordEncoder,
            String major
    ) {
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

