package com.seoultech.synergybe.domain.common.idgenerator;

import com.fasterxml.uuid.Generators;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGeneratorUUID {

    public String generateId(IdPrefix idPrefix) {
        UUID originUuid = Generators.timeBasedGenerator().generate();
        String[] uuidArr = originUuid.toString().split("-");
        String uuidStr = uuidArr[2] + uuidArr[1] + uuidArr[0] + uuidArr[3] + uuidArr[4];
        String uuid = idPrefix.getValue() + "-" + uuidStr;
        return uuid;
    }
}
