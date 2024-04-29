package com.seoultech.synergybe.domain.library;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Transactional
public class RawLibraryScheduler {
    private final RestTemplate restTemplate;

    @Scheduled(cron = "0 0 4 * * 0")
    public void updateRawLibrary() {

    }
}
