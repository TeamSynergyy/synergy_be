package com.seoultech.synergybe.domain.apply.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.ApplyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.seoultech.synergybe.domain.apply.QApply.apply;

@RequiredArgsConstructor
@Repository
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Apply findApplyByUserIdAndProjectId(String userId, String projectId) {
        return queryFactory
                .select(apply)
                .from(apply)
                .where(
                        apply.user.id.eq(userId),
                        apply.project.id.eq(projectId))
                .fetchOne();
    }

    @Override
    public List<Apply> findAllProcessByUserId(String userId) {
        return queryFactory
                .select(apply)
                .from(apply)
                .where(
                        apply.user.id.eq(userId),
                        apply.status.eq(ApplyStatus.NEW))
                .fetch();
    }

    @Override
    public List<String> findUserIdsByProjectId(String projectId) {
        return queryFactory
                .select(apply.user.id)
                .from(apply)
                .where(
                        apply.project.id.eq(projectId),
                        apply.status.eq(ApplyStatus.NEW))
                .fetch();
    }
}
