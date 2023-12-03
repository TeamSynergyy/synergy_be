package com.seoultech.synergybe.domain.ticketUser.repository;

import com.seoultech.synergybe.domain.ticketUser.TicketUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketUserRepository extends JpaRepository<TicketUser, Long> {
    Optional<TicketUser> findByTicketIdAndUserUserId(Long id, String userId);

    @Query(value = "SELECT user_id FROM ticket_user WHERE ticket_id = :ticketId", nativeQuery = true)
    List<String> findTicketUserIdsByTicketId(@Param("ticketId") Long ticketId);
}
