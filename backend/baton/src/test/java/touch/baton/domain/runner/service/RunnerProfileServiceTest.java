package touch.baton.domain.runner.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runner.service.dto.RunnerProfileRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RunnerProfileServiceTest {

    @Autowired
    private RunnerProfileService runnerProfileService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RunnerRepository runnerRepository;

    @Test
    void updateRunnerProfile() {
        //given
        Member savedMember = memberRepository.save(MemberFixture.createJudy());
        Runner saveJudy = runnerRepository.save(RunnerFixture.createRunner(savedMember));

        //when
        runnerProfileService.updateRunnerProfile(saveJudy, new RunnerProfileRequest("헤나", "우테코", "하이", new String[] {"java", "spring"}));

        Runner foundRunner = runnerRepository.joinMemberByRunnerId(saveJudy.getId()).get();

        //then
        Assertions.assertThat(foundRunner.getMember().getMemberName().getValue()).isEqualTo("헤나");
    }
}
