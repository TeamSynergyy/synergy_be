package com.seoultech.synergybe.domain.project.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoultech.synergybe.domain.project.domain.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.seoultech.synergybe.domain.project.domain.QProject.project;


@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Project> findAllByCreateAtAndLimit(Long offset) {
        return queryFactory
                .selectFrom(project)
                .orderBy(project.createAt.desc())
                .limit(10)
                .offset(offset)
                .fetch();
    }

    @Override
    public int countTotalProjectSize() {
        return queryFactory
                .selectFrom(project)
                .fetch().size();
    }
}
