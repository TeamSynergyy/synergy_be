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
import com.seoultech.synergybe.system.exception.NotProjectLeaderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
@RequiredArgsConstructor
public class RateService {
    private final RateRepository rateRepository;
    private final ProjectService projectService;
    private final UserService userService;


    public RateResponse createRate(RateRequest request, User giveUser) {
        Project project = projectService.findProjectById(request.getProjectId());
        User receiveUser = userService.findUserById(request.getReceiveUserId());
        Rate savedRate = rateRepository.save(request.toEntity(request, project, giveUser, receiveUser));

        return RateResponse.from(savedRate);
    }

    //todo
    // 모든 프로젝트의 동료 평가가 끝이 나면 평점이 매겨지고
    // 이에 대한 내용이 유저 온도에 반영되어야 한다
    // 1. 프로젝트가 끝이날 경우 클라이언트에서 projectId를 넘겨준다
    // 2. project에 속해있는 리더, 멤버들의 Id를 가져온다.
    // 3. 리더와 각 멤버들의 평점을 계산한다.
    //  - 요청한 사용자가 leader인지 체크한다.
    //  -  rate 테이블의 projectId,receiverId(멤버의 Id) 기준 평점을 가져온다
    // 3. 평점을 계산하고 온도에 반영한다
    // 4. 온도 반영 비율은 + 평점 / 10 이다
    // 5. ex 평점 3점시 기존온도 + 0.3
    public List<UserRateResponse> updateTemperature(Long projectId, User leader) {
        // check leader
        checkLeader(projectId, leader);

        List<UserRateResponse> userRateResponseList = new ArrayList<>();

        List<User> projectUsers = projectService.getUserListByProject(projectId);

        for (User user : projectUsers) {
            userRateResponseList.add(CalculateUserRate(projectId, user));
        }

        return userRateResponseList;
    }

    private UserRateResponse CalculateUserRate(Long projectId, User user) {
        List<Rate> rates = rateRepository.findAllByProjectIdAndReceiverId(projectId, user.getUserId());
        int total = 0;

        for (Rate rate : rates) {
            total += rate.getScore();
        }

        double updatedTemp = ((double) total / 10) + user.getTemperature();
        DecimalFormat df = new DecimalFormat("#.####");
        double roundedTemp = Double.parseDouble(df.format(updatedTemp));
        User updatedUser = user.updateTemperature(roundedTemp);

        return UserRateResponse.from(updatedUser, ((double) total / 10), roundedTemp);
    }

    private void checkLeader(Long projectId, User leader) {
        Project project = projectService.findProjectById(projectId);
        if (!Objects.equals(project.getLeaderId(), leader.getUserId())) {
            throw new NotProjectLeaderException();
        }
    }

    public List<RateResponse> getRateListByProject(Long projectId) {
        List<Rate> rates = rateRepository.findAllByProjectId(projectId);

        return RateResponse.from(rates);
    }
}
