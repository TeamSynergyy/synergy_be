package com.seoultech.synergybe.domain.rate.repository;

import com.seoultech.synergybe.domain.rate.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, Long> {

}
