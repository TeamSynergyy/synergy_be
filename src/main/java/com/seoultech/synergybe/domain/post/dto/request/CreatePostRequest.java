package com.seoultech.synergybe.domain.post.dto.request;

import com.seoultech.synergybe.domain.image.Image;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.postlike.PostLike;
import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class CreatePostRequest {
    private String title;
    private String content;

    private List<MultipartFile> files;

    public Post toEntity(User user, List<Image> images) {
        List<PostLike> likes = new ArrayList<>();
        return Post.builder()
                .user(user)
                .thumbnailImageId(images.get(0).getId())
                .images(images)
                .title(title)
                .content(content)
                .likes(likes)
                .build();
    }

    public Post toEntity(User user) {
        List<PostLike> likes = new ArrayList<>();
        return Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .likes(likes)
                .build();
    }
}
