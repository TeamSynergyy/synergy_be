package com.seoultech.synergybe.domain.comment.repository;

import com.seoultech.synergybe.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Long> findCommentIdsByPostId(Long postId);
}
