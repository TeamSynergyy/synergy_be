package com.seoultech.synergybe.domain.library.scheduler;

import com.seoultech.synergybe.domain.library.domain.PublicLibrary;
import com.seoultech.synergybe.domain.library.domain.RawPublicLibrary;
import com.seoultech.synergybe.domain.library.repository.PublicLibraryRepository;
import com.seoultech.synergybe.domain.library.repository.RawPublicLibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicLibraryScheduler {
    private final PublicLibraryRepository publicLibraryRepository;
    private final RawPublicLibraryRepository rawPublicLibraryRepository;

    //    @Scheduled(cron = "0 31 15 * * 4", zone = "Asia/Seoul")
    @Scheduled(fixedDelay = 20000)
    public void updatePublicLibrary() {
        log.info("update Public Library");

        List<RawPublicLibrary> rawPublicLibraryList = rawPublicLibraryRepository.findAll();

        List<PublicLibrary> publicLibraries = rawPublicLibraryList.stream().map(
                rawPublicLibrary -> PublicLibrary.builder()
                        .name(rawPublicLibrary.getName())
                        .address(rawPublicLibrary.getAddress())
                        .telNumber(rawPublicLibrary.getTelNumber())
                        .homepageUrl(rawPublicLibrary.getHompageUrl())
                        .opTime(rawPublicLibrary.getOpTime())
                        .closeDate(rawPublicLibrary.getCloseDate())
                        .build()
        ).toList();

        // 약 120개로 데이터가 크지 않은점을 고려
        publicLibraryRepository.saveAll(publicLibraries);
    }
}
