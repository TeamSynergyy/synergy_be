package com.seoultech.synergybe.domain.user;

import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import com.seoultech.synergybe.domain.user.vo.UserEmail;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomPasswordEncoder passwordEncoder;

    @Test
    void findByUserToken() {
        // given
        String userToken = "usr_token";
        User user = User.builder()
                .id(1L)
                .userToken(userToken)
                .email("email@email.com")
                .password("password")
                .major("major")
                .name("name")
                .passwordEncoder(passwordEncoder)
                .build();
        userRepository.save(user);

        // when
        User savedUser = userRepository.findByUserToken(userToken).orElseThrow();

        // then
        assertThat(savedUser.getUserToken()).isEqualTo(userToken);
    }

    @Test
    void existsByEmail() {
        // given
        String userEmail = "email@email.com";
        User user = User.builder()
                .id(1L)
                .userToken("userToken")
                .email(userEmail)
                .password("password")
                .major("major")
                .name("name")
                .passwordEncoder(passwordEncoder)
                .build();
        userRepository.save(user);
        UserEmail userEmailEmbed = new UserEmail(userEmail);

        // when
        boolean isExist = userRepository.existsByEmail(userEmailEmbed);

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    void findByEmail() {
        String userEmail = "email@email.com";
        User user = User.builder()
                .id(1L)
                .userToken("userToken")
                .email(userEmail)
                .password("password")
                .major("major")
                .name("name")
                .passwordEncoder(passwordEncoder)
                .build();
        userRepository.save(user);

        // when
        User savedUser = userRepository.findByEmail(userEmail);

        // then
        assertThat(savedUser).isNotNull();
    }

    @Test
    void findAllByUserToken() {
        String userToken = "usr_token";
        String userToken2 = "usr_token2";
        User user = User.builder()
                .id(1L)
                .userToken(userToken)
                .email("email@email.com")
                .password("password")
                .major("major")
                .name("name")
                .passwordEncoder(passwordEncoder)
                .build();

        User user2 = User.builder()
                .id(2L)
                .userToken(userToken2)
                .email("email@email.com")
                .password("password")
                .major("major")
                .name("name")
                .passwordEncoder(passwordEncoder)
                .build();
        userRepository.save(user);
        userRepository.save(user2);

        // when
        List<User> users = userRepository.findAllByUserToken(List.of(userToken, userToken2));

        // then
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void findAllByUserId() {
        Long userId = 1L;
        Long userId2 = 2L;
        User user = User.builder()
                .id(userId)
                .userToken("userToken")
                .email("email@email.com")
                .password("password")
                .major("major")
                .name("name")
                .passwordEncoder(passwordEncoder)
                .build();

        User user2 = User.builder()
                .id(userId2)
                .userToken("userToken2")
                .email("email@email.com")
                .password("password")
                .major("major")
                .name("name")
                .passwordEncoder(passwordEncoder)
                .build();
        userRepository.save(user);
        userRepository.save(user2);

        // when
        List<User> users = userRepository.findAllByUserId(List.of(userId, userId2));

        // then
        assertThat(users.size()).isEqualTo(2);
    }



}
