package com.seoultech.synergybe.domain.apply;

import com.seoultech.synergybe.base.BaseRepositoryTest;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ApplyRepositoryTest extends BaseRepositoryTest {

    @Nested
    @DisplayName("이름")
    class FindApplyList{

        @Test
        @DisplayName("applyList")
        void findApplyList() {
            //given
            User user = User.builder().build();
            Project project = Project.builder().build();
            Apply applyNew = Apply.builder().user(user).project(project).build();
            Apply applyReject = Apply.builder().user(user).project(project).build();

            applyReject.changeStatusToReject();

            userRepository.save(user);
            projectRepository.save(project);
            applyRepository.saveAll(
                    List.of(applyNew, applyReject)
            );
        }
    }
}
