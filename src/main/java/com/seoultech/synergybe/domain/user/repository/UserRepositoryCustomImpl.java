package com.seoultech.synergybe.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoultech.synergybe.domain.user.dto.response.GetUserAccountResponse;
import com.seoultech.synergybe.domain.user.dto.response.QGetUserAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.seoultech.synergybe.domain.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public GetUserAccountResponse findUserAccountByEmail(String email) {
        GetUserAccountResponse getUserAccountResponse = queryFactory
                .select(new QGetUserAccountResponse(
                        user.email,
                        user.name,
                        user.major,
                        user.temperature
                ))
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();
        return getUserAccountResponse;
    }
}
