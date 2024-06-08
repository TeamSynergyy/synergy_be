package com.seoultech.synergybe.domain.common.idgenerator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class IdGeneratorUUIDTest {
    @Autowired
    IdGenerator idGenerator;

    @Test
    void generateUUID() {
//        List<String> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            String postId = idGenerator.generateId(IdPrefix.POST);
            System.out.println("postId : " + i + " 번째 : "+ postId);
//            list.add(postId);
        }



//        assertThat()
    }


}