package com.seoultech.synergybe.domain.post.repository;

import com.seoultech.synergybe.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    @Query(value = "SELECT * FROM post WHERE post_id IN :ids ORDER BY FIELD(post_id, :ids)", nativeQuery = true)
    List<Post> findAllByIdInOrderByListOrder(@Param("ids") List<String> ids);

    Post findByPostToken(String token);
}
