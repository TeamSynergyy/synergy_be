package com.seoultech.synergybe.domain.user.service;

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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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

    public List<User> getUsers(String userIds) {
        return userRepository.findAllByUserId(userIds);
    }

    public Page<UserResponse> searchAllUsers(String keyword, Pageable pageable) {
        Specification<User> spec = this.search(keyword);

        Page<User> users = userRepository.findAll(spec, pageable);

        return UserResponse.from(users);
    }

    private Specification<User> search(String keyword) {
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
}

