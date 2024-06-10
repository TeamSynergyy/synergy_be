package com.seoultech.synergybe.domain.library.scheduler;

import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.library.domain.RawSmallLibrary;
import com.seoultech.synergybe.domain.library.domain.SmallLibrary;
import com.seoultech.synergybe.domain.library.repository.RawSmallLibraryRepository;
import com.seoultech.synergybe.domain.library.repository.SmallLibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmallLibraryScheduler {
    private final SmallLibraryRepository smallLibraryRepository;
    private final RawSmallLibraryRepository rawSmallLibraryRepository;
    private final IdGenerator idGenerator;

    @Scheduled(cron = "0 0 4 * * 6", zone = "Asia/Seoul")
    public void updateSmallLibrary() {
        log.info("update Small Library");

        List<RawSmallLibrary> rawSmallLibraryList = rawSmallLibraryRepository.findAll();

        List<SmallLibrary> smallLibraries = rawSmallLibraryList.stream().map(
                rawSmallLibrary -> SmallLibrary.builder()
                        .id(idGenerator.generateId(IdPrefix.SMALL_LIBRARY))
                        .name(rawSmallLibrary.getName())
                        .address(rawSmallLibrary.getAddress())
                        .telNumber(rawSmallLibrary.getTelNumber())
                        .homepageUrl(rawSmallLibrary.getHompageUrl())
                        .opTime(rawSmallLibrary.getOpTime())
                        .closeDate(rawSmallLibrary.getCloseDate())
                        .location(new Point(rawSmallLibrary.getLatitude(), rawSmallLibrary.getLongitude()))
                        .build()
        ).toList();

        // 약 1000개로 데이터가 크지 않은점을 고려

        smallLibraryRepository.saveAll(smallLibraries);
    }

}
