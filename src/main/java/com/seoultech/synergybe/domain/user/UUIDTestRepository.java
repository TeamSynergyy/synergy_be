package com.seoultech.synergybe.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UUIDTestRepository extends JpaRepository<UUIDTest, String> {
}
