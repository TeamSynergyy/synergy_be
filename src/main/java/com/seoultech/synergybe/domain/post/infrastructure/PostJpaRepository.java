package com.seoultech.synergybe.domain.post.infrastructure;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostJpaRepository {
    private final PostRepository postRepository;

    public void save(Post post) {
        postRepository.save(post);
    }

    public void saveAll(List<Post> postList) {
        postRepository.saveAll(postList);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public Post findByToken(String token) {
        return postRepository.findByPostToken(token);
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다."));
    }

    public List<Post> findAllByEndId(String postId) {
        return postRepository.findAllByEndId(postId);
    }

    public List<Post> findAllRecentByCount(Long offset) {
        return postRepository.findAllRecentByCount(offset);
    }

    public Long countSize() {
        return postRepository.countSize();
    }

    public Long countPostSizeByUserId(String userId) {
        return postRepository.totalSizeUser(userId);
    }

    public List<Post> findAllByFollowerIds(List<String> followingIds) {
        return postRepository.findAllByFollowerIds(followingIds);
    }

    public List<Post> WeekBest() {
        return postRepository.WeekBest();
    }

    public List<Post> findAllByUserId(String userId) {
        return postRepository.findAllByUserId(userId);
    }
}
