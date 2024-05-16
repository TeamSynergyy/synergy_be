package com.seoultech.synergybe.domain.post.repository;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostReader {
    private final PostRepository postRepository;

    public Post read(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다."));
    }





}
