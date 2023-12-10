package com.seoultech.synergybe.domain.recommend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RecommendService {

    @Scheduled(cron = "0 20 4 * * *",zone = "Asia/Seoul")
    public void fitModel() {
        RestTemplate restTemplate = new RestTemplate();
        log.info("rest template new");
        String fastApiUrl = "http://fastapi:8000"; // 컨테이너 이름과 포트
        String response = restTemplate.getForObject(fastApiUrl + "/fit_model", String.class);

        log.info("Response from FastAPI: {}", response);
    }
}
