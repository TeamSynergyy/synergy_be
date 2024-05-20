package com.seoultech.synergybe.domain.post.implement;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.data.PostJpaRepository;
import com.seoultech.synergybe.domain.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostReader {
    private final PostJpaRepository postJpaRepository;

    public Post read(String postId) {
        return postJpaRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다."));
    }





}
