package com.seoultech.synergybe.domain.post.service;

import com.seoultech.synergybe.domain.comment.dto.response.GetCommentResponse;
import com.seoultech.synergybe.domain.common.PageInfo;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.dto.response.GetListPostResponse;
import com.seoultech.synergybe.domain.post.dto.response.GetPostResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapperEntityToDto {

    public static GetListPostResponse postListToResponse(List<Post> postList, boolean hasNext) {
        List<GetPostResponse> getPostResponses = postList.stream()
                .map(
                        result -> new GetPostResponse(
                            result.getId(),
                            result.getTitle().getTitle(),
                            result.getContent().getContent(),
                            result.getUser().getId(),
                            result.getAuthorName().getAuthorName(),
                            null, // 댓글 목록을 포함
                            result.getCreateAt(),
                            result.getUpdateAt(),
                            "",
                            List.of(""),
                            result.getLikes().size() // 좋아요 수
                    )
                )
                .toList();
        PageInfo pageInfo = PageInfo.of(getPostResponses.size(), hasNext);

        return new GetListPostResponse(getPostResponses, pageInfo);
    }

//    public static GetListPostResponse postListToResponse(
//            List<Post> postList
//    ) {
//        List<GetPostResponse> getPostResponses = postList.stream()
//                .map(result -> {
//
//                    List<GetCommentResponse> commentResponses = result.getComments().stream()
//                            .map(comment -> new GetCommentResponse(
//                                    comment.getId(),
//                                    comment.getUser().getId(),
//                                    comment.getPost().getId(),
//                                    comment.getComment().getContent(),
//                                    comment.getUpdateAt()
//                            ))
//                            .toList();
//
//                    new GetPostResponse(
//                        result.getId(),
//                        result.getTitle().getTitle(),
//                        result.getContent().getContent(),
//                        result.getUser().getId(),
//                        result.getAuthorName().getAuthorName(),
//                        result.getComments(),
//                        result.c
//
//                        result.getCreateAt(),
//                        result.getUpdateAt(),
//                        result.getComments().
//                        result.getLikes().size()
//
//                        }
//
//
//                ))
//    }

}
