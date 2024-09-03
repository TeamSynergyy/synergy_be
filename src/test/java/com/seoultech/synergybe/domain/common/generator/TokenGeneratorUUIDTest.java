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

    @DisplayName("객체 ID 1만개 생성시 1초 이내로 실행된다.")
    @Test
    void generateLongId() {
        List<Long> longList = new ArrayList<>();

        // test start
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            Long longId = idGenerator.generateId();
            longList.add(longId);
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // test end
        assertThat(longList).hasSize(10000);
        assertThat(elapsedTime).isLessThanOrEqualTo(1000);
    }

    @DisplayName("대체키 ID 1만개 생성시 3초 이내로 실행된다.")
    @Test
    void generateStringId() {
        List<String> stringList = new ArrayList<>();

        // test start
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            String stringId = tokenGenerator.generateToken(IdPrefix.POST);
            stringList.add(stringId);
        }

        // test end
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        assertThat(stringList).hasSize(10000);
        assertThat(elapsedTime).isLessThanOrEqualTo(3000);
    }
}