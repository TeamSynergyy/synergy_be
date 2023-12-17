package com.seoultech.synergybe.domain.schedule.controller;

import com.seoultech.synergybe.domain.schedule.dto.request.ScheduleRequest;
import com.seoultech.synergybe.domain.schedule.dto.response.ScheduleResponse;
import com.seoultech.synergybe.domain.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "일정 api")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "일정 생성", description = "프로젝트에 대해 일정이 생성되며, 날짜 및 내용이 포함됩니다.")
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@RequestBody ScheduleRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(request));
    }

    @Operation(summary = "일정 목록", description = "해당 프로젝트의 일정들을 반환합니다.")
    @GetMapping("/{projectId}")
    public ResponseEntity<List<ScheduleResponse>> getListSchedule(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getScheduleList(projectId));
    }

    @Operation(summary = "일정 수정", description = "요청된 내용에 따라 일정이 수정됩니다.")
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable("scheduleId") Long scheduleId,@RequestBody ScheduleRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.updateSchedule(request, scheduleId));
    }

    @Operation(summary = "일정 삭제", description = "일정이 삭제됩니다.")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> deleteSchedule(@PathVariable("scheduleId") Long scheduleId) {

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.deleteSchedule(scheduleId));
    }
}
