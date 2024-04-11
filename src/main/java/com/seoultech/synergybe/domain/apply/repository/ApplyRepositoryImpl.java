package com.seoultech.synergybe.domain.apply.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.ApplyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.seoultech.synergybe.domain.apply.QApply.apply;

@RequiredArgsConstructor
@Repository
public class ApplyRepositoryImpl implements ApplyRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Apply> findByUserIdAndProjectId(String userId, Long projectId) {
        return Optional.ofNullable(queryFactory
                .select(apply)
                .from(apply)
                .where(apply.user.userId.eq(userId).and(apply.project.id.eq(projectId)))
                .fetchOne());
    }

    @Override
    public List<Apply> findAllProcessByUserId(String userId) {
        return queryFactory
                .select(apply)
                .from(apply)
                .where(apply.user.userId.eq(userId).and(apply.status.eq(ApplyStatus.PROCESS)))
                .fetch();
    }

    @Override
    public List<String> findUserIdsByProjectId(Long projectId) {
        return queryFactory
                .select(apply.user.userId)
                .from(apply)
                .where(apply.project.id.eq(projectId).and(apply.status.eq(ApplyStatus.PROCESS)))
                .fetch();
    }

    @Override
    public List<Long> findProjectIdsByUserId(String userId) {
        return null;
    }
}
