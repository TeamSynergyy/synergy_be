package com.seoultech.synergybe.domain.rate.repository;

import com.seoultech.synergybe.domain.rate.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query(value = "SELECT * FROM rate WHERE project_id = :projectId AND receive_user_id = :receiverUserId", nativeQuery = true)
    List<Rate> findAllByProjectIdAndReceiverId(@Param("projectId") Long projectId, @Param("receiverUserId") String receiveUserId);
}
