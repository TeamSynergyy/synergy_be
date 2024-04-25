package com.seoultech.synergybe.domain.rate.service;

import com.seoultech.synergybe.domain.common.idgenerator.IdGenerator;
import com.seoultech.synergybe.domain.common.idgenerator.IdPrefix;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.exception.ProjectLeaderBadRequestException;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.rate.Rate;
import com.seoultech.synergybe.domain.rate.dto.request.CreateRateRequest;
import com.seoultech.synergybe.domain.rate.dto.response.GetRateResponse;
import com.seoultech.synergybe.domain.rate.dto.response.UserRateResponse;
import com.seoultech.synergybe.domain.rate.repository.RateRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
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
    private final IdGenerator idGenerator;


    public GetRateResponse createRate(CreateRateRequest request, User giveUser) {
        Project project = projectService.findProjectById(request.projectId());
        User receiveUser = userService.getUser(request.receiveUserId());
        String rateId = idGenerator.generateId(IdPrefix.RATE);
        Rate rate = Rate.builder()
                .id(rateId).project(project).giveUser(giveUser).receiveUser(receiveUser).content(request.content()).score(request.score())
                .build();
        rateRepository.save(rate);

        return GetRateResponse.builder().build();
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
    public List<UserRateResponse> updateTemperature(String projectId, User leader) {
        // check leader
        checkLeader(projectId, leader);

        List<UserRateResponse> userRateResponseList = new ArrayList<>();

        List<User> projectUsers = projectService.getUserListByProject(projectId);

        for (User user : projectUsers) {
            userRateResponseList.add(CalculateUserRate(projectId, user));
        }

        return userRateResponseList;
    }

    private UserRateResponse CalculateUserRate(String projectId, User user) {
        List<Rate> rates = rateRepository.findAllByProjectIdAndReceiverId(projectId, user.getId());
        int total = 0;

        for (Rate rate : rates) {
            total += rate.getScore().getScore();
        }

        double updatedTemp = ((double) total / 10) + user.getTemperature().getTemperature();
        DecimalFormat df = new DecimalFormat("#.####");
        double roundedTemp = Double.parseDouble(df.format(updatedTemp));
//        User updatedUser = user.updateTemperature(roundedTemp);

        return UserRateResponse.builder().build();
    }

    private void checkLeader(String projectId, User leader) {
        Project project = projectService.findProjectById(projectId);
        if (!Objects.equals(project.getLeaderId(), leader.getId())) {
            throw new ProjectLeaderBadRequestException("프로젝트 리더가 잘못되었습니다.");
        }
    }

    public ListResponse<GetRateResponse> getRateListByProject(String projectId) {
        List<Rate> rates = rateRepository.findAllByProjectId(projectId);

        return new ListResponse(rates);
    }
}
