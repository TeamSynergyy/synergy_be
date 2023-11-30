package com.seoultech.synergybe.domain.schedule.controller;

import com.seoultech.synergybe.domain.schedule.dto.request.ScheduleRequest;
import com.seoultech.synergybe.domain.schedule.dto.response.ScheduleResponse;
import com.seoultech.synergybe.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@RequestBody ScheduleRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(request));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<List<ScheduleResponse>> getListSchedule(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getScheduleList(projectId));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable("scheduleId") Long scheduleId,@RequestBody ScheduleRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.updateSchedule(request, scheduleId));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> deleteSchedule(@PathVariable("scheduleId") Long scheduleId) {

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.deleteSchedule(scheduleId));
    }
}
