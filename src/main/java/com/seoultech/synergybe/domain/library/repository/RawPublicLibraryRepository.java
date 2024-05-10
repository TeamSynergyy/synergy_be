package com.seoultech.synergybe.domain.library.repository;

import com.seoultech.synergybe.domain.library.domain.RawPublicLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawPublicLibraryRepository extends JpaRepository<RawPublicLibrary, Long> {
}
