package com.seoultech.synergybe.domain.chat.jpa_repository;

import com.seoultech.synergybe.domain.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(value = "SELECT * from chat_room where create_user_id = :userId or attend_user_id = :userId", nativeQuery = true)
    List<ChatRoom> findAllByCreateUserIdOrAttendUserId(@Param("userId") String userId);
}
