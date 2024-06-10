package com.seoultech.synergybe.domain.common.generator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenGeneratorUUIDTest {
    @Autowired
    IdGenerator idGenerator;

    @Test
    void generateUUID() {
//        List<String> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Long postId = idGenerator.generateId();
            System.out.println("postId : " + i + " 번째 : "+ postId);
//            list.add(postId);
        }



//        assertThat()
    }


}