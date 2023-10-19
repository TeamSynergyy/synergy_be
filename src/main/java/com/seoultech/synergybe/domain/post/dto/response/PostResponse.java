package com.seoultech.synergybe.domain.post.dto.response;

import com.seoultech.synergybe.domain.comment.Comment;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private String authorName;
    private String authorId;

//    @Nullable
//    private int likes;
//    @Nullable
//    private List<Comment> comments;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getUser().getUsername(), post.getUser().getUserId(),
                 post.getCreateAt(), post.getUpdateAt());
//        post.getLikes().size(), post.getComments(),
    }

    public static Page<PostResponse> from(Page<Post> posts) {
        return posts.map(post -> PostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getUser().getUsername())
                .authorId(post.getUser().getUserId())
//                .likes(post.getComments().size())
//                .comments(post.getComments())
                .createAt(post.getCreateAt())
                .updateAt(post.getUpdateAt())
                .build()
        );
    }

    public static List<PostResponse> from(List<Post> posts) {
        return posts.stream()
                .map(post -> PostResponse.builder()
                                .postId(post.getId())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .authorName(post.getUser().getUsername())
                                .authorId(post.getUser().getUserId())
//                                .likes(post.getComments().size())
//                                .comments(post.getComments())
                                .createAt(post.getCreateAt())
                                .updateAt(post.getUpdateAt())
                                .build())
                .collect(Collectors.toList());
    }
}
