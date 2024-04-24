package com.seoultech.synergybe.domain.projectuser.repository;

import com.seoultech.synergybe.domain.projectuser.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, String> {
    Optional<ProjectUser> findByProjectIdAndUserUserId(String projectId, String userId);

    @Query(value = "SELECT user_id FROM project_user WHERE project_id = :projectId", nativeQuery = true)
    List<String> findProjectUserIdsByProjectId(@Param("projectId") String projectId);

    @Query(value = "SELECT project_id FROM project_user WHERE user_id = :userId", nativeQuery = true)
    List<String> findProjectIdsByUserId(@Param("userId") String userId);
}
