package com.seoultech.synergybe.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.seoultech.synergybe.domain.common.RandomNumber;
import com.seoultech.synergybe.domain.email.MailService;
import com.seoultech.synergybe.domain.user.dto.request.CreateUserRequest;
import com.seoultech.synergybe.domain.user.dto.request.ValidateNumberRequest;
import com.seoultech.synergybe.domain.user.exception.UserBadRequestException;
import com.seoultech.synergybe.domain.user.repository.UserRefreshTokenFactory;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import com.seoultech.synergybe.domain.user.service.UserService;

import com.seoultech.synergybe.system.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RandomNumber randomNumber;

    private String authNumber;

    private final String TRUE = "true";
    private final String FALSE = "false";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Autowired
    private UserRefreshTokenFactory userRefreshTokenFactory;

    @PostConstruct
    public void setAuthNumber() {
        this.authNumber = randomNumber.generateRandomNumber();
    }

    @DisplayName("회원가입시 인증 번호를 이메일로 전송한다")
    @Test
    @Order(1)
    void sendValidateEmail() throws Exception {
        // given
        String email = "jonghuncu@gmail.com";

        // CountDownLatch 설정: 비동기 작업이 완료될 때까지 대기
        CountDownLatch latch = new CountDownLatch(1);

        // when
        mailService.sendValidateEmail(email, authNumber);

        // latch가 5초 이내에 0이 되길 기다림 (비동기 작업의 완료를 기다림)
        latch.await(5, TimeUnit.SECONDS);

        // redis에서 이메일 정보 가져옴
        String getEmail = redisUtil.getData(authNumber);

        // then
        assertThat(email).isEqualTo(getEmail);
    }



    @DisplayName("회원가입시 인증 번호를 인증한다")
    @Test
    @Order(2)
    void validateNumber() {
        // given
        String email = "jonghuncu@gmail.com";
        ValidateNumberRequest request = new ValidateNumberRequest(email, authNumber);
        redisUtil.setDataExpire(authNumber, email, 60*5L);

        // when
        userService.validateNumber(request);
        String result = redisUtil.getData(email + authNumber);

        // then
        assertThat(result).isEqualTo(TRUE);
    }

    @DisplayName("회원가입시 올바르지 않은 인증 번호로 인증을 실패한다")
    @Test
    @Order(3)
    void failValidateNumber() {
        // given
        String email = "a@gmail.com";
        String failAuthNumber = "123456";
        ValidateNumberRequest request = new ValidateNumberRequest(email, failAuthNumber);
        redisUtil.setDataExpire(authNumber, email, 60*5L);

        // when then
        assertThatThrownBy(() -> userService.validateNumber(request))
                .isInstanceOf(UserBadRequestException.class)
                .hasMessage("validateNumber >> 유저 email: " + email + " 이메일 인증시 인증번호가 일치하지 않습니다.");
    }



    @DisplayName("회원가입 유저를 등록한다.")
    @Test
    @Order(4)
    void createUser() {
        // given
        String email = "jonghuncu@gmail.com";
        String password = "password";
        String name = "name";
        String major = "major";
        CreateUserRequest request = new CreateUserRequest(email, password, name, major, authNumber);
        redisUtil.setDataExpire(email+authNumber, TRUE, 60*5L);

        // when
        String userToken = userService.createUser(request);
        User user = userRepository.findByUserToken(userToken).orElseThrow();

        // then
        assertThat(user.getName().getName()).isEqualTo(name);
        assertThat(user.getEmail().getEmail()).isEqualTo(email);
    }

    @DisplayName("인증되지 않은 유저는 회원가입을 실패한다.")
    @Test
    @Order(4)
    void failCreateUser() {
        // given
        String email = "jonghuncu@gmail.com";
        String password = "password";
        String name = "name";
        String major = "major";
        CreateUserRequest request = new CreateUserRequest(email, password, name, major, authNumber);
        redisUtil.setDataExpire(email+authNumber, FALSE, 60*5L);

        // when then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(UserBadRequestException.class)
                .hasMessage("createUser >> 유저 email: " + email + " 인증되지 않은 사용자입니다.");
    }

    @DisplayName("이메일 중복 체크")
    @Test
    @Order(5)
    void checkEmailDuplicate() {
        // given
        String email = "jonghuncu@gmail.com";
        String password = "password";
        String name = "name";
        String major = "major";
        CreateUserRequest request = new CreateUserRequest(email, password, name, major, authNumber);
        redisUtil.setDataExpire(email+authNumber, TRUE, 60*5L);
        userService.createUser(request);

        // when then
        assertThatThrownBy(() -> userService.checkEmailDuplicate(email))
                .isInstanceOf(UserBadRequestException.class)
                .hasMessage("checkEmailDuplicate >> 유저 email: " + email + "은 이미 존재합니다.");
    }
    @DisplayName("RefreshToken이 없는 경우 예외를 발생시킨다")
    @Test
    void EmptyRefreshTokengenerateAccessTokenByRefreshToken() {
        // given
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        Cookie mockCookie = new Cookie("refreshToken", null);
        request.setCookies(mockCookie);

        // when & then
        assertThrows(UserBadRequestException.class,
                () -> userService.generateAccessTokenByRefreshToken(request, response));
    }

    @DisplayName("RefreshToken이 유효한 경우 AccessToken을 재발급한다")
    @Test
    void generateAccessTokenByRefreshToken_V1() {
        // given
        String email = "jonghuncu@gmail.com";
        String password = "password";
        String name = "name";
        String major = "major";
        CreateUserRequest createUserRequest = new CreateUserRequest(email, password, name, major, authNumber);
        redisUtil.setDataExpire(email+authNumber, TRUE, 60*5L);

        String userToken = userService.createUser(createUserRequest);
        User user = userRepository.findByUserToken(userToken).orElseThrow();
        Long userId = user.getId();

        // refresh token
        UserRefreshToken userRefreshToken = new UserRefreshToken(userId);
        userRefreshTokenFactory.save(userRefreshToken);

        String refreshToken = userRefreshToken.getRefreshToken().getRefreshToken();

        // cookie 세팅
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        Cookie mockCookie = new Cookie("refreshToken", refreshToken);
        request.setCookies(mockCookie);

        // when
        userService.generateAccessTokenByRefreshToken(request, response);

        // then
        String accessToken = response.getHeader("Authorization");  // Assuming the token is in the "Authorization" header
        assertThat(accessToken).isNotNull();
        assertThat(accessToken).startsWith("Bearer ");
    }

    @DisplayName("유효하지 않은 RefreshToken인 경우 예외를 발생시킨다")
    @Test
    void invalidRefreshTokengenerateAccessTokenByRefreshToken() {
        // given
        String email = "jonghuncu@gmail.com";
        String password = "password";
        String name = "name";
        String major = "major";
        CreateUserRequest createUserRequest = new CreateUserRequest(email, password, name, major, authNumber);
        redisUtil.setDataExpire(email+authNumber, TRUE, 60*5L);

        String userToken = userService.createUser(createUserRequest);
        User user = userRepository.findByUserToken(userToken).orElseThrow();
        Long userId = user.getId();

        // refresh token
        UserRefreshToken userRefreshToken = new UserRefreshToken(userId);
        userRefreshTokenFactory.save(userRefreshToken);

        String refreshToken = userRefreshToken.getRefreshToken().getRefreshToken();
        String invalidRefreshToken = refreshToken + "invalid";

        // cookie 세팅
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        Cookie mockCookie = new Cookie("refreshToken", invalidRefreshToken);
        request.setCookies(mockCookie);

        // when & then
        assertThrows(UserBadRequestException.class,
                () -> userService.generateAccessTokenByRefreshToken(request, response));
    }
}