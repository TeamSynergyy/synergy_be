package com.seoultech.synergybe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SynergyBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SynergyBeApplication.class, args);
    }

}
