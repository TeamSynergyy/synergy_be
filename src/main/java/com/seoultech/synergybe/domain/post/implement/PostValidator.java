package com.seoultech.synergybe.domain.post.implement;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.exception.PostBadRequestException;
import com.seoultech.synergybe.domain.post.vo.PostTitle;
import com.seoultech.synergybe.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class PostValidator {
    private PostTitle postTitle;
    public void validateUser(User user, Post post) {
        if (!post.getUser().equals(user)) {
            throw new PostBadRequestException("인증되지 않은 유저입니다.");
        }
    }
}
