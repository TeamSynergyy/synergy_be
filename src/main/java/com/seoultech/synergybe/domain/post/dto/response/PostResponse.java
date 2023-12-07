package com.seoultech.synergybe.domain.post.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.seoultech.synergybe.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private String userId;
    private String authorName;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateAt;

    private String thumbnailImageUrl;

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
                 post.getCreateAt(), post.getUpdateAt(), imagesUrl.get(0), imagesUrl, post.getLikes().size());
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
                .map(post -> {
                    PostResponse.PostResponseBuilder builder = PostResponse.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .authorName(post.getUser().getUsername())
                            .userId(post.getUser().getUserId())
                            .likes(post.getLikes().size())
                            .createAt(post.getCreateAt())
                            .updateAt(post.getUpdateAt());

                    if (post.getImages() != null && !post.getImages().isEmpty()) {
                        builder.thumbnailImageUrl(post.getImages().get(0).getStoreFileName());
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());
    }

    public static List<PostResponse> fromEmpty(List<Post> posts) {
        return posts.stream()
                .map(post -> PostResponse.builder().build()).collect(Collectors.toList());
    }
}
