package com.seoultech.synergybe.domain.projectuser.repository;

import com.seoultech.synergybe.domain.projectuser.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    Optional<ProjectUser> findByProjectIdAndUserUserId(Long projectId, String userId);
}
