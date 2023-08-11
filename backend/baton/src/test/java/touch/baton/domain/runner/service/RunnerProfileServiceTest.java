package touch.baton.domain.runner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.service.dto.RunnerUpdateRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;

class RunnerProfileServiceTest extends ServiceTestConfig {

    private RunnerService runnerService;

    @BeforeEach
    void setUp() {
        runnerService = new RunnerService(runnerRepository, runnerTechnicalTagRepository, technicalTagRepository);
    }

    @DisplayName("Runner 의 프로필을 수정한다.")
    @Test
    void updateRunnerProfile() {
        // given
        final Member memberJudy = memberRepository.save(MemberFixture.createJudy());
        final Runner runnerJudy = runnerRepository.save(RunnerFixture.createRunner(memberJudy));
        final RunnerUpdateRequest runnerUpdateRequest = new RunnerUpdateRequest("변경된 이름", "변경된 회사", "변경된 자기소개", List.of("changedTag1", "changedTag2"));

        // when, then
        assertThatCode(() -> runnerService.updateRunnerProfile(runnerJudy, runnerUpdateRequest))
                .doesNotThrowAnyException();
    }
}
