package com.seoultech.synergybe.base;

import com.seoultech.synergybe.base.config.TestQueryDslConfig;
import com.seoultech.synergybe.domain.apply.repository.ApplyRepository;
import com.seoultech.synergybe.domain.common.CustomPasswordEncoder;
import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.project.repository.ProjectRepository;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({TestQueryDslConfig.class})
public class BaseRepositoryTest {
    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected ApplyRepository applyRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected IdGenerator idGenerator;

    @Autowired
    protected CustomPasswordEncoder passwordEncoder;


}
