package com.seoultech.synergybe.domain.schedule.repository;

import com.seoultech.synergybe.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
