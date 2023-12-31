package com.seoultech.synergybe.domain.user.repository;

import com.seoultech.synergybe.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);

    @Query(value = "SELECT * FROM user WHERE user_id IN :userIds", nativeQuery = true)
    List<User> findAllByUserId(@Param("userIds") List<String> userIds);

    Page<User> findAll(Specification<User> spec, Pageable pageable);
}
