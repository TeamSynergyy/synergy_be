package com.seoultech.synergybe.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.seoultech.synergybe.domain.user.dto.request.CreateUserRequest;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import com.seoultech.synergybe.domain.user.service.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저를 등록한다.")
    @Test
    void createUser() {
        // given
        String email = "jonghuncu@gmail.com";
        String password = "password";
        String name = "name";
        String major = "major";
        String validationNumber = "123456";
        CreateUserRequest request = new CreateUserRequest(email, password, name, major, validationNumber);

        // when
        String userToken = userService.createUser(request);
        User user = userRepository.findByUserToken(userToken).orElseThrow();

        // then
        assertThat(user.getName().getName()).isEqualTo(name);
        assertThat(user.getEmail().getEmail()).isEqualTo(email);
    }
}
