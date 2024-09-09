package com.seoultech.synergybe.domain.post;

import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;
import com.seoultech.synergybe.domain.post.repository.PostRepository;
import com.seoultech.synergybe.domain.post.service.PostService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;
import static org.mockito.Mockito.*;
@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomPasswordEncoder passwordEncoder;

    @DisplayName("캐싱 함수를 통해 호출시 캐싱된 결과가 반환되어 호출을 1회만 한다.")
    @Test
    void getWeekBestPostList() {
        // given
        User user = User.builder().id(1000L).userToken("token").passwordEncoder(passwordEncoder).email("as@gmail.com").password("password").name("name").major("major").build();
        userRepository.save(user);
        when(postRepository.findAllByLikeAndDate()).thenReturn(List.of(Post.builder().id(1L).title("title").content("content").user(user).build()));

        // when
        IntStream.range(0, 10)
                .forEach(i -> postService.getWeekBestPostList());

        // then
        verify(postRepository, times(1)).findAllByLikeAndDate();
    }

}
