package com.seoultech.synergybe.domain.schedule.service;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.project.domain.Project;
import com.seoultech.synergybe.domain.project.domain.service.ProjectService;
import com.seoultech.synergybe.domain.schedule.Schedule;
import com.seoultech.synergybe.domain.schedule.dto.request.CreateScheduleRequest;
import com.seoultech.synergybe.domain.schedule.dto.response.GetScheduleResponse;
import com.seoultech.synergybe.domain.schedule.exception.ScheduleNotFoundException;
import com.seoultech.synergybe.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ProjectService projectService;

    @Transactional
    public void createSchedule(CreateScheduleRequest request) {
        Project project = projectService.findProjectById(request.projectId());
        Schedule savedSchedule = Schedule.builder()
                .project(project)
                .build();

        scheduleRepository.save(savedSchedule);
        savedSchedule.addProject(project);
    }


    @Transactional(readOnly = true)
    public ListResponse<GetScheduleResponse> getScheduleList(String projectId) {
        Project project = projectService.findProjectById(projectId);

        return new ListResponse(project.getSchedules());
    }

    public void updateSchedule(CreateScheduleRequest request, String scheduleId) {
        Schedule schedule = this.findScheduleById(scheduleId);
//        Schedule updatedSchedule = schedule.updateSchedule(request);
        scheduleRepository.save(schedule);
    }

    private Schedule findScheduleById(String scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 일정입니다."));
    }

    public void deleteSchedule(String scheduleId) {
        Schedule schedule = this.findScheduleById(scheduleId);
        scheduleRepository.delete(schedule);
    }
}
