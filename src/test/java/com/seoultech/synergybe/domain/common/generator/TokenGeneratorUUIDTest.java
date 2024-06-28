package com.seoultech.synergybe.domain.common.generator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenGeneratorUUIDTest {
    @Autowired
    IdGenerator idGenerator;

    @Autowired
    TokenGenerator tokenGenerator;

    @Test
    void generateLongId() {
        List<Long> longList = new ArrayList<>();

        // test start
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            Long longId = idGenerator.generateId();
            System.out.println("longId : " + i + " 번째 : "+ longId);
            longList.add(longId);

        }

        // test end
        long endTime = System.currentTimeMillis();
        System.out.println("longId Total execution time: " + (endTime - startTime) + " milliseconds");

        assertThat(longList.size()).isEqualTo(10000);
    }

    @Test
    void generateStringId() {
        List<String> stringList = new ArrayList<>();

        // test start
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            String stringId = tokenGenerator.generateToken(IdPrefix.POST);
            System.out.println("stringId : " + i + " 번째 : "+ stringId);
            stringList.add(stringId);
        }

        // test end
        long endTime = System.currentTimeMillis();
        System.out.println("stringId Total execution time: " + (endTime - startTime) + " milliseconds");

        assertThat(stringList.size()).isEqualTo(10000);
    }


}