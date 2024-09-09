package com.seoultech.synergybe.domain.post;

import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;
import com.seoultech.synergybe.domain.post.repository.PostRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomPasswordEncoder passwordEncoder;

    @DisplayName("offset 기준 그 다음의 limit 개수 까지 post를 조회한다.")
    @Test
    void findAllByCreateAtAndLimit() {
        int limit = 20;
        long totalSize = 30L;
        long offset = 20L;
        long initialId = 0L;
        int lastIndex = limit - 1;
        User user = User.builder().id(1000L).userToken("token").passwordEncoder(passwordEncoder).email("as@gmail.com").password("password").name("name").major("major").build();
        userRepository.save(user);

        // 0 - 29
        for (long i=initialId; i<totalSize; i++) {
            postRepository.save(Post.builder().id(i).title("title").content("content").user(user).build());
        }

        List<Post> response = postRepository.findAllByCreateAtAndLimit(offset);

        assertThat(response).hasSize(limit);
        assertThat(response.get(lastIndex).getId()).isEqualTo(initialId);
    }



}
