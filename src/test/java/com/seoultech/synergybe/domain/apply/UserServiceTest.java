package com.seoultech.synergybe.domain.apply;

import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;
import com.seoultech.synergybe.domain.common.idgenerator.IdGenerator;
import com.seoultech.synergybe.domain.common.idgenerator.IdPrefix;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import com.seoultech.synergybe.domain.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private IdGenerator idGenerator;

    @Spy
    private CustomPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유저 10만개 생성")
    void generate() throws Exception {
        List<User> userList = new ArrayList<>();
        String userId;
        List<String> strings = new ArrayList<>();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        for (int i=0; i < 100000; i++) {
            userId = idGenerator.generateId(IdPrefix.USER);

            User user = User.builder()
                    .id(userId)
                    .email("email@gmail.com")
                    .password("password0000")
                    .name("name")
                    .passwordEncoder(passwordEncoder)
                    .major("major")
                    .build();

            strings.add(userId);

            userList.add(user);
        }
        System.out.println("postList = " + userList.size());

        int batchSize = 1000; // 배치 단위 크기
        int listSize = userList.size();; // 전체 데이터 크기
        for (int i=0; i<listSize; i += batchSize) {
            int endIndex = i + batchSize; // 최소 index를 구한다 save를 할때 batch 단위만큼 subList로 저장하기 위해서
            List<User> batchList = userList.subList(i, endIndex);
            userRepository.saveAll(batchList);
            userRepository.flush();
        }

        Stream<String> stringStream = strings.stream();
        stringStream.forEach(System.out::println);

        Assertions.assertThat(userList.size()).isEqualTo(10000);
    }

}
