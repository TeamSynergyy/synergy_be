package com.seoultech.synergybe.domain.apply.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.ApplyStatus;
import com.seoultech.synergybe.domain.apply.dto.response.GetApplyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static com.seoultech.synergybe.domain.apply.QApply.apply;

@RequiredArgsConstructor
@Repository
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Apply> findApplyByUserIdAndProjectId(String userId, String projectId) {
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
                .where(apply.user.userId.eq(userId).and(apply.status.eq(ApplyStatus.NEW)))
                .fetch();
    }

    @Override
    public List<String> findUserIdsByProjectId(String projectId) {
        return queryFactory
                .select(apply.user.userId)
                .from(apply)
                .where(apply.project.id.eq(projectId).and(apply.status.eq(ApplyStatus.NEW)))
                .fetch();
    }

    @Override
    public List<Long> findProjectIdsByUserId(String userId) {
        return null;
    }
}
