package com.seoultech.synergybe.domain.post.repository;

import com.seoultech.synergybe.domain.post.Post;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM post WHERE post_id < :postId ORDER BY post_id DESC LIMIT 10", nativeQuery = true)
    List<Post> findAllByEndId(@Param("postId") Long postId);

    @Query(value = "SELECT * FROM post WHERE user_id = :userId", nativeQuery = true)
    List<Post> findAllByUserId(@Param("userId") String userId);

    List<Post> findAll(Specification<Post> spec);
}