package com.seoultech.synergybe.domain.apply.repository;

import com.seoultech.synergybe.domain.apply.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {

    @Query(value = "SELECT * FROM apply WHERE user_id = :userId AND project_id = :projectId", nativeQuery = true)
    Optional<Apply> findByUserIdAndProjectId(@Param("userId") String userId, @Param("projectId") Long projectId);

    @Query(value = "SELECT * FROM apply WHERE user_id = :userId", nativeQuery = true)
    List<Apply> findAllByUserId(@Param("userId") String userId);

    @Query(value = "SELECT user_id FROM apply WHERE project_id = :projectId", nativeQuery = true)
    List<String> findUserIdsByProjectId(@Param("projectId") Long projectId);
}
