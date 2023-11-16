package com.seoultech.synergybe.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.follow.repository.FollowRepository;
import com.seoultech.synergybe.domain.user.dto.request.UpdateUserRequest;
import com.seoultech.synergybe.domain.user.dto.response.ListUserResponse;
import com.seoultech.synergybe.domain.user.dto.response.UserIdsResponse;
import com.seoultech.synergybe.domain.user.dto.response.UserResponse;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.exception.NotExistUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    public UserResponse getMyInfo(User user) {
        return UserResponse.from(user);
    }

    public UserResponse getUserInfo(String userId) {
        return UserResponse.from(this.findUserById(userId));
    }

    public User findUserById(String userId) {
        return userRepository.findByUserId(userId);
    }

    public List<User> getUsers(List<String> userIds) {
        return userRepository.findAllByUserId(userIds);
    }

    public Page<UserResponse> searchAllUsers(String keyword, Pageable pageable) {
        Specification<User> spec = this.search(keyword);

        Page<User> users = userRepository.findAll(spec, pageable);

        return UserResponse.from(users);
    }

    public Specification<User> search(String keyword) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);

                try {
                    return cb.or(
                            cb.like(userRoot.get("username"), "%" + keyword + "%"),
                            cb.like(userRoot.get("email"), "%" + keyword + "%")
                    );
                } catch (Exception e) {
                    log.error("search toPredicate Error {}", e.getMessage());
                    throw new NotExistUserException();
                }
            }
        };
    }

    public UserResponse updateMyInfo(User user, UpdateUserRequest request) {
        User updatedUser = userRepository.save(user.update(request));

        return UserResponse.from(updatedUser);
    }

    public ListUserResponse getRecommendListByUser(User user, Long end) {

        try {
            log.info("get recommend user list start");
            String userId = user.getUserId();
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
                return ListUserResponse.from(UserResponse.fromEmpty(users));
            }

            // end 기준 end ~ end + 10 순서에 있는 게시글 가져오기
            int startIdx = end.intValue();
            int endIdx = Math.min(startIdx + 10, userIds.size());

            List<String> result = userIds.subList(startIdx, endIdx);

            List<User> users = userRepository.findAllByUserId(result);


            log.info("Response from FastAPI: {}", response);
            return ListUserResponse.from(UserResponse.from(users));
        } catch (Exception e) {
            log.error(">> 추천 유저 가져오기 실패 {}", e.getMessage());
            throw new NotExistUserException();
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

    public UserIdsResponse getFollowerIds(User user) {

        return UserIdsResponse.from(followRepository.findFollowerIdsByFollowingId(user.getUserId()));
    }

    public UserIdsResponse getFollowingIds(User user) {

        return UserIdsResponse.from(followRepository.findFollowingIdsByFollowerId(user.getUserId()));
    }
}

