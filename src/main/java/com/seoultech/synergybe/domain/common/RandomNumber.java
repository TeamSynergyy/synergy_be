package com.seoultech.synergybe.domain.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.random.RandomGenerator;

@Slf4j
@Component
public class RandomNumber {

    public String generateRandomNumber() {
        // L128X256MixRandom
        RandomGenerator generator = RandomGenerator.of("L128X256MixRandom");

        String s1 = String.valueOf(generator.nextInt(10));
        String s2 = String.valueOf(generator.nextInt(10));
        String s3 = String.valueOf(generator.nextInt(10));
        String s4 = String.valueOf(generator.nextInt(10));
        String s5 = String.valueOf(generator.nextInt(10));
        String s6 = String.valueOf(generator.nextInt(10));

        log.info("random : " + s1 + s2 + s3 + s4 + s5 + s6);

        return s1 + s2 + s3 + s4 + s5 + s6;
    }
}
