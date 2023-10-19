package com.seoultech.synergybe.domain.apply.repository;

import com.seoultech.synergybe.domain.apply.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {

    Optional<Apply> findByUserUserIdAndProjectId(String userId, Long projectId);
}
