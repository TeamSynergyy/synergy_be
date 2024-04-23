package com.seoultech.synergybe.domain.schedule.dto.response;

import com.seoultech.synergybe.domain.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record GetScheduleResponse(
        String scheduleId,
        String title,
        String content,
        String label,
        LocalDateTime startAt,
        LocalDateTime endAt
) {

//    public static GetScheduleResponse from(Schedule schedule) {
//        return new GetScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getLabel(),
//                schedule.getStartAt(), schedule.getEndAt());
//    }
//
//    public static List<GetScheduleResponse> from(List<Schedule> schedules) {
//        return schedules.stream()
//                .map(schedule -> GetScheduleResponse.builder()
//                        .scheduleId(schedule.getId())
//                        .title(schedule.getTitle())
//                        .content(schedule.getContent())
//                        .label(schedule.getLabel())
//                        .startAt(schedule.getStartAt())
//                        .endAt(schedule.getEndAt())
//                        .build())
//                .collect(Collectors.toList());
//    }
}
