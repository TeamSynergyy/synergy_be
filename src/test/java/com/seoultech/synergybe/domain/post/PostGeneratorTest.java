package com.seoultech.synergybe.domain.post;

import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;
import com.seoultech.synergybe.domain.common.idgenerator.IdGenerator;
import com.seoultech.synergybe.domain.common.idgenerator.IdPrefix;
import com.seoultech.synergybe.domain.post.implement.PostFactory;
import com.seoultech.synergybe.domain.post.implement.PostManager;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import com.seoultech.synergybe.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostGeneratorTest {
    private static final int TEST_COUNT = 10000;

    @Autowired
    CustomPasswordEncoder passwordEncoder;

    @Autowired
    IdGenerator idGenerator;

    @Autowired
    PostManager postManager;

    @Autowired
    UserRepository userRepository;


    @Test
    void generatePost() {
        User user = User.builder()
                .id("user-id")
                .password("3e4r5t6y6y7u")
                .passwordEncoder(passwordEncoder)
                .email("email@email.com")
                .name("jonghun")
                .major("cs")
                .build();
        userRepository.save(user);

        assertThat("email@email.com").isEqualTo(user.getEmail().getEmail());

        List<Post> postList = new ArrayList<>();


        for (int i = 0; i < 10000; i++) {
            String postId = idGenerator.generateId(IdPrefix.POST);
            Post post = Post.builder()
                    .id(postId)
                    .user(user)
                    .title("title")
                    .content("content")
                    .build();
            postList.add(post);

        }
        postManager.saveAll(postList);
    }
}
