package com.seoultech.synergybe.domain.post.data;

import com.seoultech.synergybe.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final PostJpaRepository postJpaRepository;

    public void save(Post post) {
        postJpaRepository.save(post);
    }

    public List<Post> findAllByEndId(String postId) {
        return postJpaRepository.findAllByEndId(postId);
    }
}
