package com.seoultech.synergybe.domain.library.repository;

import com.seoultech.synergybe.domain.library.domain.RawLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawLibraryRepository extends JpaRepository<RawLibrary, Long> {
}
