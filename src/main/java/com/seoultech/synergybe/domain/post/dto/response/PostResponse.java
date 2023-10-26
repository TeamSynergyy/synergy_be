package com.seoultech.synergybe.domain.post.dto.response;

import com.seoultech.synergybe.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

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
    private String userId;
    private String authorName;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private List<String> imagesUrl;

    private int likes;

    public PostResponse(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userId = post.getUser().getUserId();
        this.authorName = post.getAuthorName();
        this.createAt = post.getCreateAt();
        this.updateAt = post.getUpdateAt();
        this.likes = post.getLikes().size();
    }

    public static PostResponse from(Post post) {
        return new PostResponse(post);
    }

    public static PostResponse from(Post post, List<String> imagesUrl) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getUser().getUserId(), post.getUser().getUsername(),
                 post.getCreateAt(), post.getUpdateAt(), imagesUrl, post.getLikes().size());
    }

    public static Page<PostResponse> from(Page<Post> posts) {
        return posts.map(post -> PostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUser().getUserId())
                .authorName(post.getUser().getUsername())
                .likes(post.getLikes().size())
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
                    .userId(post.getUser().getUserId())
                    .likes(post.getLikes().size())
                    .createAt(post.getCreateAt())
                    .updateAt(post.getUpdateAt())
                    .build())
                .collect(Collectors.toList());
    }
}
