package com.seoultech.synergybe.domain.apply.repository;

import com.seoultech.synergybe.domain.apply.Apply;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplyRepositoryCustom {
    Optional<Apply> findApplyByUserIdAndProjectId(String userId, String projectId);

    List<Apply> findAllProcessByUserId(String userId);

    List<String> findUserIdsByProjectId(String projectId);

    List<Long> findProjectIdsByUserId(String userId);
}
