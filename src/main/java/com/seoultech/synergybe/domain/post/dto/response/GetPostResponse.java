package com.seoultech.synergybe.domain.post.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.seoultech.synergybe.domain.comment.dto.response.GetCommentResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GetPostResponse(
        String postId,
        String title,
        String content,
        String userId,
        String authorName,

        List<GetCommentResponse> commentList,

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime createAt,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime updateAt,
        String thumbnailImageUrl,
        List<String> imagesUrl,
        int likes

) {


//    public static GetPostResponse from(Post post) {
//        return new GetPostResponse(post);
//    }
//
//    public static GetPostResponse from(Post post, List<String> imagesUrl) {
//        return new GetPostResponse(post.getId(), post.getTitle(), post.getContent(), post.getUser().getUserId(), post.getUser().getUsername(),
//                 post.getCreateAt(), post.getUpdateAt(), imagesUrl.get(0), imagesUrl, post.getLikes().size());
//    }
//
//    public static Page<GetPostResponse> from(Page<Post> posts) {
//        return posts.map(post -> GetPostResponse.builder()
//                .postId(post.getId())
//                .title(post.getTitle())
//                .content(post.getContent())
//                .userId(post.getUser().getUserId())
//                .authorName(post.getUser().getUsername())
//                .likes(post.getLikes().size())
//                .createAt(post.getCreateAt())
//                .updateAt(post.getUpdateAt())
//                .build()
//        );
//    }
//
//    public static List<GetPostResponse> from(List<Post> posts) {
//        return posts.stream()
//                .map(post -> {
//                    GetPostResponse.PostResponseBuilder builder = GetPostResponse.builder()
//                            .postId(post.getId())
//                            .title(post.getTitle())
//                            .content(post.getContent())
//                            .authorName(post.getUser().getUsername())
//                            .userId(post.getUser().getUserId())
//                            .likes(post.getLikes().size())
//                            .createAt(post.getCreateAt())
//                            .updateAt(post.getUpdateAt());
//
//                    if (post.getImages() != null && !post.getImages().isEmpty()) {
//                        builder.thumbnailImageUrl(post.getImages().get(0).getStoreFileName());
//                    }
//
//                    return builder.build();
//                })
//                .collect(Collectors.toList());
//    }
//
//    public static List<GetPostResponse> fromEmpty(List<Post> posts) {
//        return posts.stream()
//                .map(post -> GetPostResponse.builder().build()).collect(Collectors.toList());
//    }
}
