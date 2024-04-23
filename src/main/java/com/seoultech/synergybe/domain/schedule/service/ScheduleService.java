package com.seoultech.synergybe.domain.schedule.service;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.schedule.Schedule;
import com.seoultech.synergybe.domain.schedule.dto.request.CreateScheduleRequest;
import com.seoultech.synergybe.domain.schedule.dto.response.GetScheduleResponse;
import com.seoultech.synergybe.domain.schedule.exception.ScheduleNotFoundException;
import com.seoultech.synergybe.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ProjectService projectService;

    public GetScheduleResponse createSchedule(CreateScheduleRequest request) {
        Project project = projectService.findProjectById(request.getProjectId());
        Schedule savedSchedule = scheduleRepository.save(request.toEntity(project));
        savedSchedule.addProject(project);

        return GetScheduleResponse.from(savedSchedule);
    }


    @Transactional(readOnly = true)
    public List<GetScheduleResponse> getScheduleList(Long projectId) {
        Project project = projectService.findProjectById(projectId);

        return GetScheduleResponse.from(project.getSchedules());
    }

    public GetScheduleResponse updateSchedule(CreateScheduleRequest request, Long scheduleId) {
        Schedule schedule = this.findScheduleById(scheduleId);
        Schedule updatedSchedule = schedule.updateSchedule(request);
        scheduleRepository.save(updatedSchedule);

        return GetScheduleResponse.from(updatedSchedule);
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 일정입니다."));
    }

    public GetScheduleResponse deleteSchedule(Long scheduleId) {
        Schedule schedule = this.findScheduleById(scheduleId);
        scheduleRepository.delete(schedule);

        return GetScheduleResponse.from(schedule);
    }
}
