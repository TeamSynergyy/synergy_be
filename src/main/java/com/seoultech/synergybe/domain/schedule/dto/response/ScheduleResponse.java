package com.seoultech.synergybe.domain.schedule.dto.response;

import com.seoultech.synergybe.domain.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
public class ScheduleResponse {
    private Long scheduleId;
    private String title;
    private String content;
    private String label;
    private LocalDateTime startAt;
    private LocalDateTime endAt;


    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getLabel(),
                schedule.getStartAt(), schedule.getEndAt());
    }

    public static List<ScheduleResponse> from(List<Schedule> schedules) {
        return schedules.stream()
                .map(schedule -> ScheduleResponse.builder()
                        .scheduleId(schedule.getId())
                        .title(schedule.getTitle())
                        .content(schedule.getContent())
                        .label(schedule.getLabel())
                        .startAt(schedule.getStartAt())
                        .endAt(schedule.getEndAt())
                        .build())
                .collect(Collectors.toList());
    }
}
