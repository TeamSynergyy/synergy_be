package com.seoultech.synergybe.domain.post.implement;

import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Component
@RequiredArgsConstructor
public class PostFactory {
    private final IdGenerator idGenerator;
    public Post createPost(User user, String title, String content, List<MultipartFile> files) {
        String postId = idGenerator.generateId(IdPrefix.POST);

        return Post.builder()
                .id(postId)
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
