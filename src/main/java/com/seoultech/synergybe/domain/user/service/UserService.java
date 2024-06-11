package com.seoultech.synergybe.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;
import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.generator.TokenGenerator;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.email.MailService;
import com.seoultech.synergybe.domain.user.UserRefreshToken;
import com.seoultech.synergybe.domain.user.dto.response.*;
import com.seoultech.synergybe.domain.user.exception.UserBadRequestException;
import com.seoultech.synergybe.domain.user.exception.UserNotFoundException;
import com.seoultech.synergybe.domain.user.repository.UserRefreshTokenRepository;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.vo.UserEmail;
import com.seoultech.synergybe.system.exception.ErrorCode;
import com.seoultech.synergybe.system.security.JwtUtil;
import com.seoultech.synergybe.system.utils.CookieUtil;
import com.seoultech.synergybe.system.utils.EmailRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRefreshTokenReader userRefreshTokenReader;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final CustomPasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;
    private final TokenGenerator tokenGenerator;
    private final MailService mailService;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;

    @Transactional
    public String createUser(
            String email,
            String password,
            String name,
            String major
    ) {
        checkEmailDuplicate(email);

        Long userId = idGenerator.generateId();
        String userToken = tokenGenerator.generateToken(IdPrefix.USER);

        User user = User.builder()
                .id(userId)
                .userToken(userToken)
                .email(email)
                .password(password)
                .name(name)
                .passwordEncoder(passwordEncoder)
                .major(major)
                .build();
        userRepository.save(user);

        mailService.validateEmail(email);

        return user.getUserToken();
    }

    private void checkEmailDuplicate(String email) {
        UserEmail userEmail = new UserEmail(email);
        boolean isEmailDuplicated = userRepository.existsByEmail(userEmail);
        if (isEmailDuplicated) {
            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }
    }

    public User getUserByToken(String userToken) {
        return userRepository.findByUserToken(userToken).orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
    }

    public GetUserAccountResponse getUserInfo(String userId) {
        User user = getUserByToken(userId);

        return GetUserAccountResponse.builder()
                .userToken(user.getUserToken())
                .email(user.getEmail().getEmail())
                .major(user.getMajor().getMajor())
                .name(user.getName().getName())
                .temperature(user.getTemperature().getTemperature())
                .build();
    }

    public List<User> getUsers(List<Long> userIds) {
        return userRepository.findAllByUserId(userIds);
    }

    public Page<User> searchAllUsers(String keyword, Pageable pageable) {
        Specification<User> spec = this.search(keyword);

        Page<User> users = userRepository.findAll(spec, pageable);

        return users;
    }

    public Specification<User> search(String keyword) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);

                try {
                    return cb.or(
                            cb.like(userRoot.get("username"), "%" + keyword + "%"),
                            cb.like(userRoot.get("organization"), "%" + keyword + "%"),
                            cb.like(userRoot.get("skills"), "%" + keyword + "%"),
                            cb.like(userRoot.get("email"), "%" + keyword + "%")
                    );
                } catch (Exception e) {
                    log.error("search toPredicate Error {}", e.getMessage());
                    throw new UserNotFoundException("존재하지 않는 유저입니다.");
                }
            }
        };
    }

    @Transactional
    public void updateMyInfo(
            String userId,
            String email,
            String name,
            String major
    ) {
        User user = getUserByToken(userId);
        user.updateUserInfo(email, name, major);
    }

    public ListResponse<GetUserAccountResponse> getSimilarUserListByUser(String userId, Long end) {

        try {
            log.info("user Id {}", userId);

            RestTemplate restTemplate = new RestTemplate();
            log.info("rest template new");
            String fastApiUrl = "http://fastapi:8000"; // 컨테이너 이름과 포트
            String response = restTemplate.getForObject(fastApiUrl + "/users/similar/" + userId, String.class);

            log.info("Response from FastAPI: {}", response);

            List<Long> userIds = this.extractIds(response);

            // 빈 배열일 경우 빈 배열 리턴
            if (userIds.isEmpty()) {
                List<User> users = new ArrayList<>();
                return new ListResponse(users);
//                return ListUserResponse.from(UserResponse.fromEmpty(users));
            }

            // end 기준 end ~ end + 10 순서에 있는 게시글 가져오기
            int startIdx = end.intValue();
            int endIdx = Math.min(startIdx + 10, userIds.size());

            List<Long> result = userIds.subList(startIdx, endIdx);

            List<User> users = userRepository.findAllByUserId(result);


            log.info("Response from FastAPI: {}", response);
            return new ListResponse(users);
//            return ListUserResponse.from(UserResponse.from(users));
        } catch (Exception e) {
            log.error(">> 추천 유저 가져오기 실패 {}", e.getMessage());
            throw new UserNotFoundException("존재하지 않는 유저입니다.");
        }
    }

    private List<Long> extractIds(String response) {
        try {
            // 받은 JSON 응답을 자바 리스트로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            log.error(">> http cliend response body {}", response);

            return objectMapper.readValue(response, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void validateEmail(EmailRequest request) {
        boolean isAuthorize = mailService.checkAuthNumber(request.email(), request.authNumber());

        if (!isAuthorize) {
            // todo
            // 인증번호가 다르다면 회원가입 진행하지 않음
        }
    }

    // todo
    // 만약 쿠키가 userRefreshToken 과 일치한다면
    // accessToken 재발급 진행
    @Transactional
    public void generateAccessTokenByRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> refreshTokenCookie = CookieUtil.getCookie(request, "refreshToken");

        if (refreshTokenCookie.isEmpty()) {
            throw new UserBadRequestException(ErrorCode.UN_AUTHORIZATION, "empty() refreshToken 입니다.");
        }
        String refreshToken = refreshTokenCookie.get().getValue();
        log.info("existing refreshToken value: " + refreshToken);
        UserRefreshToken getRefreshToken = userRefreshTokenReader.readByRefreshToken(refreshToken);

        getRefreshToken.updateRefreshToken();
        log.info("new refreshToken value: " + getRefreshToken.getRefreshToken().getRefreshToken());

        Long userId = getRefreshToken.getUserId();

        User user = getUserById(userId);
        String email = user.getEmail().getEmail();
        String accessToken = jwtUtil.createToken(user.getId(), email);
        log.info("refreshToken save success");
        cookieUtil.addRefreshTokenCookie(response, getRefreshToken, accessToken);
        log.info("add refreshToken cookie");
    }

    public List<Long> getUserIds(List<String> userToken) {
        List<User> users = userRepository.findAllByUserToken(userToken);

        List<Long> userIds = users.stream().map(user -> user.getId()).toList();
        return userIds;
    }
}

