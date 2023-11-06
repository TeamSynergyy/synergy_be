package com.seoultech.synergybe.domain.comment.repository;

import com.seoultech.synergybe.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT comment_id FROM comment WHERE post_id = :postId", nativeQuery = true)
    List<Long> findCommentIdsByPostId(@Param("postId") Long postId);
}
