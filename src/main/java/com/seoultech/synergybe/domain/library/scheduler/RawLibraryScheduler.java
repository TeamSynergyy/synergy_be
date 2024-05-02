package com.seoultech.synergybe.domain.library.scheduler;

import com.seoultech.synergybe.domain.library.repository.RawPublicLibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Transactional
public class RawLibraryScheduler {
    private final RestTemplate restTemplate;
    private final RawPublicLibraryRepository rawPublicLibraryRepository;

    @Value("library.api.key")
    private String libraryApiKey;
    private Long dataCount;
    private final Long BATCH_SIZE = 500L;

    // 데이터 총 개수를 가져와서 dataCount에 넣어줍니다.
    @Scheduled(cron = "0 0 4 * * 0")
    public void updateRawLibrary() {

    }
}
