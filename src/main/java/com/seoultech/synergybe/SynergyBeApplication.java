package com.seoultech.synergybe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.seoultech.synergybe.system.config.properties")
public class SynergyBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynergyBeApplication.class, args);
    }

}
