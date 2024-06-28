package com.seoultech.synergybe.domain.post.implement;

import com.seoultech.synergybe.domain.comment.dto.response.GetCommentResponse;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.presentation.dto.response.GetPostResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapperEntityToDto {

    public static List<GetPostResponse> postListToResponse(List<Post> postList) {
        List<GetPostResponse> getPostResponses = postList.stream()
                .map(
                        result -> new GetPostResponse(
                            result.getPostToken(),
                            result.getTitle().getTitle(),
                            result.getContent().getContent(),
                            result.getUser().getUserToken(),
                            result.getAuthorName().getAuthorName(),
                            result.getComments().stream().map(
                                    comment -> new GetCommentResponse(
                                            comment.getCommentToken(),
                                            comment.getUser().getUserToken(),
                                            comment.getPost().getPostToken(),
                                            comment.getComment().getContent(),
                                            comment.getUpdateAt()
                                    )
                            ).collect(Collectors.toList()), // 댓글 목록을 포함
                            result.getCreateAt(),
                            result.getUpdateAt(),
                            "",
                            List.of(""),
                            result.getLikes().size() // 좋아요 수
                    )
                )
                .toList();
//        PageInfo pageInfo = PageInfo.of(getPostResponses.size(), hasNext);

        return getPostResponses;
    }

    public static GetPostResponse postToResponse(Post post) {
        GetPostResponse getPostResponse = new GetPostResponse(
                post.getPostToken(),
                post.getTitle().getTitle(),
                post.getContent().getContent(),
                post.getUser().getUserToken(),
                post.getAuthorName().getAuthorName(),
                post.getComments().stream().map(
                        comment -> new GetCommentResponse(
                                comment.getCommentToken(),
                                comment.getUser().getUserToken(),
                                comment.getPost().getPostToken(),
                                comment.getComment().getContent(),
                                comment.getUpdateAt()
                        )
                ).collect(Collectors.toList()),
                post.getCreateAt(),
                post.getUpdateAt(),
                "",
                List.of(""),
                post.getLikes().size()
        );
        return getPostResponse;
    }
}
