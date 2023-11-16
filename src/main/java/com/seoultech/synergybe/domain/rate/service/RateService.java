package com.seoultech.synergybe.domain.rate.service;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.rate.Rate;
import com.seoultech.synergybe.domain.rate.dto.request.RateRequest;
import com.seoultech.synergybe.domain.rate.dto.response.RateResponse;
import com.seoultech.synergybe.domain.rate.dto.response.UserRateResponse;
import com.seoultech.synergybe.domain.rate.repository.RateRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class RateService {
    private final RateRepository rateRepository;
    private final ProjectService projectService;
    private final UserService userService;


    public RateResponse createRate(RateRequest request) {
        Project project = projectService.findProjectById(request.getProjectId());
        User giveUser = userService.findUserById(request.getGiveUserId());
        User receiveUser = userService.findUserById(request.getReceiveUserId());
        Rate savedRate = rateRepository.save(request.toEntity(request, project, giveUser, receiveUser));

        return RateResponse.from(savedRate);
    }

    //todo
    // 모든 프로젝트의 동료 평가가 끝이 나면 평점이 매겨지고
    // 이에 대한 내용이 유저 온도에 반영되어야 한다
    // 1. 프로젝트가 끝이날 경우 클라이언트에서 projectId를 넘겨준다
    // 2. rate 테이블의 projectId,receiverId 기준 평점을 가져온다
    // 3. 평점을 계산하고 온도에 반영한다
    // 4. 온도 반영 비율은 + 평점 / 10 이다
    // 5. ex 평점 3점시 기존온도 + 0.3
    public UserRateResponse updateTemperature(Long projectId, User user) {
        List<Rate> rates = rateRepository.findAllByProjectIdAndReceiverId(projectId, user.getUserId());
        int total = 0;

        for (Rate rate : rates) {
            total += rate.getScore();
        }

        double updatedTemp = ((double) total / 10) + user.getTemperature();
        User updatedUser = user.updateTemperature(updatedTemp);

        return UserRateResponse.from(updatedUser, ((double) total / 10));
    }
}
