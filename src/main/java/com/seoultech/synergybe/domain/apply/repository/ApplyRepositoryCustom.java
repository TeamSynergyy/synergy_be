package com.seoultech.synergybe.domain.apply.repository;

import com.seoultech.synergybe.domain.apply.Apply;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplyRepositoryCustom {
    Optional<Apply> findByUserIdAndProjectId(@Param("userId") String userId, @Param("projectId") Long projectId);

    List<Apply> findAllProcessByUserId(@Param("userId") String userId);

    List<String> findUserIdsByProjectId(@Param("projectId") Long projectId);

    List<Long> findProjectIdsByUserId(@Param("userId") String userId);
}
