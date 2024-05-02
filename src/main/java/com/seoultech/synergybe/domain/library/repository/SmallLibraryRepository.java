package com.seoultech.synergybe.domain.library.repository;

import com.seoultech.synergybe.domain.library.domain.SmallLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmallLibraryRepository extends JpaRepository<SmallLibrary, Long> {
}
