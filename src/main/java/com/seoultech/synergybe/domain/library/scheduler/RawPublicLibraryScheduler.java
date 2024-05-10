package com.seoultech.synergybe.domain.library.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.library.domain.RawPublicLibrary;
import com.seoultech.synergybe.domain.library.dto.response.SeoulPublicLibraryInfo;
import com.seoultech.synergybe.domain.library.dto.response.SeoulPublicLibraryInfoResponse;
import com.seoultech.synergybe.domain.library.repository.RawPublicLibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RawPublicLibraryScheduler {
    private final RestTemplate restTemplate;
    private final RawPublicLibraryRepository rawPublicLibraryRepository;

    @Value("${library.api.key}")
    private String libraryApiKey;
    private int dataCount;
    private final int BATCH_SIZE = 500;


    /**
     * 크론 스케줄링
     * 첫 번째 필드: 초 (0-59)
     * 두 번째 필드: 분 (0-59)
     * 세 번째 필드: 시간 (0-23)
     * 네 번째 필드: 일 (1-31)
     * 다섯 번째 필드: 월 (1-12)
     * 여섯 번째 필드: 요일 (0-6, 일요일부터 토요일까지, 일요일=0 또는 7)
     * 데이터 총 개수를 가져와서 dataCount에 넣어줍니다.
     */
    @Scheduled(cron = "0 0 4 * * 6", zone = "Asia/Seoul")
    public void updateRawPublicLibrary() {
        log.info("================시작");
        countRawPublicLibraryTotalData();
        savePublicLibraryFromOpenApi();
        log.info("=================끝");
    }

    private void countRawPublicLibraryTotalData() {
        UriComponents uriComponents = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("openapi.seoul.go.kr")
                .port(8088)
                .path("/{libraryAPI}/json/SeoulPublicLibraryInfo/{start}/{end}")
                .buildAndExpand(libraryApiKey, 1, 10);

        log.info("uri : " + uriComponents);
        RequestEntity<Void> requestEntity = RequestEntity.get(uriComponents.toUri()).build();
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        log.info(responseEntity.getBody());
        try {
            dataCount = new JSONObject(responseEntity.getBody())
                    .getJSONObject("SeoulPublicLibraryInfo")
                    .getInt("list_total_count");
        } catch (JSONException jsonException) {
            log.error("JSONException {}", jsonException.toString());
        }

        log.info("dataCount : " + dataCount);
    }

    public void insertRawPublicLibrary(List<SeoulPublicLibraryInfo.RawLibrary> libraries) {
//        List<SeoulPublicLibraryInfo.RawLibrary> libraries = getPublicLibraryFromOpenApi();

        // stream api로
        List<RawPublicLibrary> rawPublicLibraries = libraries.stream()
                .map(library -> RawPublicLibrary.builder()
                        .publicLibrarySeq(library.getLBRRY_SEQ_NO())
                        .name(library.getLBRRY_NAME())
                        .guCode(library.getGU_CODE())
                        .guCodeValue(library.getCODE_VALUE())
                        .address(library.getADRES())
                        .telNumber(library.getTEL_NO())
                        .hompageUrl(library.getHMPG_URL())
                        .opTime(library.getOP_TIME())
                        .closeDate(library.getFDRM_CLOSE_DATE())
                        .seName(library.getLBRRY_SE_NAME())
                        .latitude(Double.valueOf(library.getXCNTS()))
                        .longitude(Double.valueOf(library.getYDNTS()))
                        .build())
                .toList();

        rawPublicLibraryRepository.saveAll(rawPublicLibraries);


    }


    private void savePublicLibraryFromOpenApi() {
        int start;
        List<SeoulPublicLibraryInfo.RawLibrary> rawLibraries = new ArrayList<>();
        for (start = 1; start <= dataCount; start += BATCH_SIZE) {

            UriComponents uriComponents = UriComponentsBuilder
                    .newInstance()
                    .scheme("http")
                    .host("openapi.seoul.go.kr")
                    .port(8088)
                    .path("/{libraryAPI}/json/SeoulPublicLibraryInfo/{start}/{end}")
                    .buildAndExpand(libraryApiKey, 1, 10);

            RequestEntity<Void> requestEntity = RequestEntity.get(uriComponents.toUri()).build();
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

            log.info("RawLibrary ======================");
            log.info("responseENtity : " + responseEntity.getBody());

            // JSON 문자열을 Java 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();

            // 내가 필요한 데이터들만 파싱하기 위해 설정
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                SeoulPublicLibraryInfoResponse seoulPublicLibraryInfoResponse = objectMapper.readValue(responseEntity.getBody() , SeoulPublicLibraryInfoResponse.class);
                log.info(String.valueOf(seoulPublicLibraryInfoResponse.getSeoulPublicLibraryInfo().getListTotalCount()));
                log.info(String.valueOf(seoulPublicLibraryInfoResponse.getSeoulPublicLibraryInfo().getRow().get(0).getHMPG_URL()));
                log.info(String.valueOf(seoulPublicLibraryInfoResponse.getSeoulPublicLibraryInfo().getRESULT().getCODE()));

                rawLibraries.addAll(seoulPublicLibraryInfoResponse.getSeoulPublicLibraryInfo().getRow());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            log.info("RawLibrary 변환중 ======================");

            // batch size로 저장
            insertRawPublicLibrary(rawLibraries);
        }
    }
}
