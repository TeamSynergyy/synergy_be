package com.seoultech.synergybe.domain.post.implement;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.business.dto.UpdatePostDto;
import com.seoultech.synergybe.domain.post.infrastructure.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
@RequiredArgsConstructor
public class PostManager {
    private final PostJpaRepository postRepository;

    public void save(Post post) {
        postRepository.save(post);
    }

    public void update(Post post, UpdatePostDto updatePostDto) {
        post.updatePost(updatePostDto.title(), updatePostDto.content());
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public void saveAll(List<Post> postList) {
        postRepository.saveAll(postList);
    }
}
