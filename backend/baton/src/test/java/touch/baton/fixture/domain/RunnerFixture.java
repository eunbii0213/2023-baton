package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;

import static touch.baton.fixture.vo.TotalRatingFixture.totalRating;

public abstract class RunnerFixture {

    private RunnerFixture() {
    }

    public static Runner create(final int totalRating, final Grade grade, final Member member) {
        return Runner.builder()
                .totalRating(totalRating(totalRating))
                .grade(grade)
                .member(member)
                .build();
    }

    public static Runner createRunner(final Member member) {
        return create(5000, Grade.BARE_FOOT, member);
    }
}