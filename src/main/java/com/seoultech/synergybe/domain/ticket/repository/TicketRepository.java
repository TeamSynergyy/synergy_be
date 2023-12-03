package com.seoultech.synergybe.domain.ticket.repository;

import com.seoultech.synergybe.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "SELECT * FROM ticket WHERE project_id = :projectId", nativeQuery = true)
    List<Ticket> findAllByProjectId(@Param("projectId") Long projectId);

    @Query(value = "SELECT * FROM ticket WHERE project_id = :projectId AND status = UPPER(:status) AND order_number >= :orderNumber", nativeQuery = true)
    List<Ticket> findAllByProjectIdAndStatusAndOrder(@Param("projectId") Long projectId, @Param("status") String status, @Param("orderNumber") Integer orderNumber);

    @Query(value = "SELECT COALESCE(COUNT(*), 1) "
            + "FROM ticket " +
            "WHERE status = UPPER(:status) AND project_id = :projectId", nativeQuery = true)
    Integer findLastOrderNumber(@Param("status") String status, @Param("projectId") Long projectId);
}
