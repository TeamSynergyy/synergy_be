package com.seoultech.synergybe.domain.apply.repository;

import com.seoultech.synergybe.domain.apply.Apply;

import java.util.List;
import java.util.Optional;

public interface ApplyRepositoryCustom {
    Apply findApplyByUserIdAndProjectId(String userId, String projectId);

    List<Apply> findAllProcessByUserId(String userId);

    List<Long> findUserIdsByProjectId(String projectId);
}
