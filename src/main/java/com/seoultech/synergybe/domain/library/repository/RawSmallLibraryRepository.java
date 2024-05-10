package com.seoultech.synergybe.domain.library.repository;

import com.seoultech.synergybe.domain.library.domain.RawSmallLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawSmallLibraryRepository extends JpaRepository<RawSmallLibrary, Long> {
}
