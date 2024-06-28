package com.seoultech.synergybe.domain.post.implement;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.infrastructure.PostJpaRepository;
import com.seoultech.synergybe.domain.post.presentation.dto.response.GetPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostReader {
    private final PostJpaRepository postJpaRepository;

    public Post read(String postId) {
        return postJpaRepository.findByToken(postId);
    }

    public ListResponse<GetPostResponse> readRecentList(Long offset) {
        List<Post> postList = postJpaRepository.findAllRecentByCount(offset);
        System.out.println("postList.size()" + postList.size());
        Long totalCount = postJpaRepository.countSize();

        boolean hasNext;
        int pageSize = 10;

        if (totalCount > pageSize + offset) {
            hasNext = true;
        } else {
            hasNext = false;
        }

        List<GetPostResponse> getPostResponses = PostMapperEntityToDto.postListToResponse(postList);

        return new ListResponse<>(getPostResponses);
    }

    public ListResponse<GetPostResponse> readFeed(List<String> followingIds) {
        List<Post> postList = postJpaRepository.findAllByFollowerIds(followingIds);

        return postListToListResponse(postList);
    }

    public ListResponse<GetPostResponse> readWeekBest() {
        List<Post> postList = postJpaRepository.WeekBest();

        return postListToListResponse(postList);
    }

    public ListResponse<GetPostResponse> readListByUser(String userId) {
        List<Post> postList = postJpaRepository.findAllByUserId(userId);

        return postListToListResponse(postList);
    }

    private ListResponse<GetPostResponse> postListToListResponse(List<Post> postList) {
        List<GetPostResponse> getPostResponses = PostMapperEntityToDto.postListToResponse(postList);

        return new ListResponse<>(getPostResponses);
    }
}
