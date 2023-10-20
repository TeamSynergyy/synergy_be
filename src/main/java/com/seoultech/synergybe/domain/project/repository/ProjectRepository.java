package com.seoultech.synergybe.domain.project.repository;

import com.seoultech.synergybe.domain.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = "SELECT * FROM project WHERE project_id < :projectId ORDER BY project_id DESC LIMIT 10", nativeQuery = true)
    List<Project> findAllByEndId(Long projectId);

    Page<Project> findAll(Specification<Project> spec, Pageable pageable);
}
