package com.seoultech.synergybe.domain.notice.repository;

import com.seoultech.synergybe.domain.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, String> {
    @Query(value = "SELECT notice_id FROM notice WHERE project_id = :projectId", nativeQuery = true)
    List<String> findNoticeIdsByProjectId(@Param("projectId") String projectId);
}
