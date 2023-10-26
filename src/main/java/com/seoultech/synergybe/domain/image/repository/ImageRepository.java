package com.seoultech.synergybe.domain.image.repository;

import com.seoultech.synergybe.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query(value = "SELECT store_file_name FROM image WHERE post_id = :postId", nativeQuery = true)
    List<String> findStoreFileNamesByPostId(@Param("postId") Long postId);
}
