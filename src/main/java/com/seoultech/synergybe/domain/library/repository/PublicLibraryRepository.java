package com.seoultech.synergybe.domain.library.repository;

import com.seoultech.synergybe.domain.library.domain.PublicLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicLibraryRepository extends JpaRepository<PublicLibrary, String> {
}
