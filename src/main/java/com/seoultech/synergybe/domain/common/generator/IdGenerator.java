package com.seoultech.synergybe.domain.common.generator;

import org.springframework.stereotype.Component;

@Component
public interface IdGenerator {
    Long generateId();
}
