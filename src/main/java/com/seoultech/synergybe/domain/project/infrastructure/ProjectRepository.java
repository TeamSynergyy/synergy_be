package com.seoultech.synergybe.domain.project.infrastructure;

import com.seoultech.synergybe.domain.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String>, ProjectRepositoryCustom {
    @Query(value = "SELECT * FROM project WHERE project_sequence < :projectSequence ORDER BY project_sequence DESC LIMIT 10", nativeQuery = true)
    List<Project> findAllByEndSequence(@Param("projectSequence") Long projectSequence);

    Page<Project> findAll(Specification<Project> spec, Pageable pageable);

    @Query(value = "SELECT * FROM project WHERE leader_id = :userId", nativeQuery = true)
    List<Project> findAllByLeaderId(@Param("userId") String userId);
}
