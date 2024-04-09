package com.seoultech.synergybe.system.common;

/**
 * ApiResponseDto / ApiResponse 둘 중 어떤 구조를 가져갈지 고민
 */

public record ApiResponseDto(int statusCode, String statusMessage) {
}
