package com.seoultech.synergybe.domain.schedule.service;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.service.ProjectService;
import com.seoultech.synergybe.domain.schedule.Schedule;
import com.seoultech.synergybe.domain.schedule.dto.request.ScheduleRequest;
import com.seoultech.synergybe.domain.schedule.dto.response.ScheduleResponse;
import com.seoultech.synergybe.domain.schedule.repository.ScheduleRepository;
import com.seoultech.synergybe.system.exception.NotExistScheduleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ProjectService projectService;

    public ScheduleResponse createSchedule(ScheduleRequest request) {
        Schedule savedSchedule = scheduleRepository.save(request.toEntity());
        Project project = projectService.findProjectById(request.getProjectId());
        savedSchedule.addProject(project);

        return ScheduleResponse.from(savedSchedule);
    }


    public List<ScheduleResponse> getScheduleList(Long projectId) {
        Project project = projectService.findProjectById(projectId);

        return ScheduleResponse.from(project.getSchedules());
    }

    public ScheduleResponse updateSchedule(ScheduleRequest request, Long scheduleId) {
        Schedule schedule = this.findScheduleById(scheduleId);
        Schedule updatedSchedule = schedule.updateSchedule(request);
        scheduleRepository.save(updatedSchedule);

        return ScheduleResponse.from(updatedSchedule);
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(NotExistScheduleException::new);
    }

    public ScheduleResponse deleteSchedule(Long scheduleId) {
        Schedule schedule = this.findScheduleById(scheduleId);

        scheduleRepository.delete(schedule);

        return ScheduleResponse.from(schedule);
    }
}
