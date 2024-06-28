package com.seoultech.synergybe.domain.idtest;

import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.generator.TokenGenerator;
import com.seoultech.synergybe.domain.user.IdTest;
import com.seoultech.synergybe.domain.user.IdTestRepository;
import com.seoultech.synergybe.domain.user.UUIDTest;
import com.seoultech.synergybe.domain.user.UUIDTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class IdGenerateTest {
    @Autowired
    IdGenerator idGenerator;

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    IdTestRepository idTestRepository;

    @Autowired
    UUIDTestRepository uuidTestRepository;

    @Test
    void generateLongId() {
        // test start
        long startTime = System.currentTimeMillis();



        for (int j = 0; j < 100; j++) {
            List<IdTest> idTestList = new ArrayList<>();

            for (int i = 0; i < 10000; i++) {
                Long id = idGenerator.generateId();
                IdTest idTest = IdTest.builder()
                        .id(id)
                        .text("text")
                        .build();

                idTestList.add(idTest);
            }
            System.out.println("longId : " + j + " 번째 : ");
            idTestRepository.saveAll(idTestList);
        }

        // test end
        long endTime = System.currentTimeMillis();
        System.out.println("longId Total execution time: " + (endTime - startTime) + " milliseconds");

    }

    @Test
    void generateStringId() {
        // test start
        long startTime = System.currentTimeMillis();



        for (int j = 0; j < 100; j++) {
            List<UUIDTest> uuidTestList = new ArrayList<>();

            for (int i = 0; i < 10000; i++) {
                String id = tokenGenerator.generateToken(IdPrefix.POST_LIKE);
                UUIDTest idTest = UUIDTest.builder()
                        .id(id)
                        .text("text")
                        .build();

                uuidTestList.add(idTest);
            }
            System.out.println("StringId : " + j + " 번째 : ");
            uuidTestRepository.saveAll(uuidTestList);
        }

        // test end
        long endTime = System.currentTimeMillis();
        System.out.println("StringId Total execution time: " + (endTime - startTime) + " milliseconds");

    }
}
