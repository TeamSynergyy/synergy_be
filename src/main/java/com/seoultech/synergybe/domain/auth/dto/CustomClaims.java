package com.seoultech.synergybe.domain.auth.dto;

import java.util.List;

public record CustomClaims(String userId, List<String> authorities) {
    public static CustomClaims of(String userId, List<String> authorities) {
        return new CustomClaims(userId, authorities);
    }
}
