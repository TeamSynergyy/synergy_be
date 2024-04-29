package com.seoultech.synergybe.domain.library.repository;

import com.seoultech.synergybe.domain.library.domain.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, String> {
}
