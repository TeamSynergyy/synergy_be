package com.seoultech.synergybe.domain.common.idgenerator;

import org.springframework.stereotype.Component;

@Component
public interface IdGenerator {
    String generateId(IdPrefix idPrefix);

}
