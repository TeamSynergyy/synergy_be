package com.seoultech.synergybe.domain.common.generator;

import com.fasterxml.uuid.Generators;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenGeneratorUUID implements TokenGenerator {

    public String generateToken(IdPrefix idPrefix) {
        UUID originUuid = Generators.timeBasedGenerator().generate();
        String[] uuidArr = originUuid.toString().split("-");
        String uuidStr = uuidArr[2] + uuidArr[1] + uuidArr[0] + uuidArr[3] + uuidArr[4];
        String uuid = idPrefix.getValue() + "_" + uuidStr;
        return uuid;
    }
}
