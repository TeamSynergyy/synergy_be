package com.seoultech.synergybe.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;
import com.seoultech.synergybe.domain.common.idgenerator.IdGenerator;
import com.seoultech.synergybe.domain.common.idgenerator.IdPrefix;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.user.dto.response.*;
import com.seoultech.synergybe.domain.user.exception.UserBadRequestException;
import com.seoultech.synergybe.domain.user.exception.UserNotFoundException;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.vo.UserEmail;
import com.seoultech.synergybe.system.exception.ErrorCode;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CustomPasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;

    @Transactional
    public String createUser(
            String email,
            String password,
            String name,
            String major
    ) {
        checkEmailDuplicate(email);

        String userId = idGenerator.generateId(IdPrefix.USER);
        User user = User.builder()
                .id(userId)
                .email(email)
                .password(password)
                .name(name)
                .passwordEncoder(passwordEncoder)
                .major(major)
                .build();
        userRepository.save(user);

        return user.getId();
    }

    private void checkEmailDuplicate(String email) {
        UserEmail userEmail = new UserEmail(email);
        boolean isEmailDuplicated = userRepository.existsByEmail(userEmail);
        if (isEmailDuplicated) {
            throw new UserBadRequestException(ErrorCode.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }
    }

    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
    }

    public GetUserAccountResponse getUserInfo(String userId) {
        User user = getUser(userId);

        return GetUserAccountResponse.builder()
                .userId(user.getId())
                .email(user.getEmail().getEmail())
                .major(user.getMajor().getMajor())
                .name(user.getName().getName())
                .temperature(user.getTemperature().getTemperature())
                .build();
    }

    public List<User> getUsers(List<String> userIds) {
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
        User user = getUser(userId);
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

            List<String> userIds = this.extractIds(response);

            // 빈 배열일 경우 빈 배열 리턴
            if (userIds.isEmpty()) {
                List<User> users = new ArrayList<>();
                return new ListResponse(users);
//                return ListUserResponse.from(UserResponse.fromEmpty(users));
            }

            // end 기준 end ~ end + 10 순서에 있는 게시글 가져오기
            int startIdx = end.intValue();
            int endIdx = Math.min(startIdx + 10, userIds.size());

            List<String> result = userIds.subList(startIdx, endIdx);

            List<User> users = userRepository.findAllByUserId(result);


            log.info("Response from FastAPI: {}", response);
            return new ListResponse(users);
//            return ListUserResponse.from(UserResponse.from(users));
        } catch (Exception e) {
            log.error(">> 추천 유저 가져오기 실패 {}", e.getMessage());
            throw new UserNotFoundException("존재하지 않는 유저입니다.");
        }
    }

    private List<String> extractIds(String response) {
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
}

