package com.seoultech.synergybe.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.vo.UserEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.seoultech.synergybe.domain.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public User findByEmail(String email) {
        UserEmail userEmail = new UserEmail(email);
        return queryFactory
                .selectFrom(user)
                .where(user.email.eq(userEmail))
                .fetchOne();
    }

    @Override
    public List<User> findAllByUserToken(List<String> userToken) {
        return queryFactory
                .selectFrom(user)
                .where(user.userToken.in(userToken))
                .fetch();
    }

    @Override
    public List<User> findAllByUserId(List<Long> userIds) {
        return queryFactory
                .selectFrom(user)
                .where(user.id.in(userIds))
                .fetch();
    }
}
