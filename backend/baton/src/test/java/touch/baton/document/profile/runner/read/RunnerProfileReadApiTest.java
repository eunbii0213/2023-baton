package touch.baton.document.profile.runner.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.controller.RunnerProfileController;
import touch.baton.domain.runner.service.RunnerService;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.List;
import java.util.Optional;

import static javax.swing.text.html.parser.DTDConstants.NUMBER;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.spy;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

@WebMvcTest(RunnerProfileController.class)
class RunnerProfileReadApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostService runnerPostService;

    @MockBean
    private RunnerService runnerService;

    @BeforeEach
    void setUp() {
        restdocsSetUp(new RunnerProfileController(runnerPostService, runnerService));
    }

    @DisplayName("러너 본인 프로필 조회 API")
    @Test
    void readMyProfileByToken() throws Exception {
        // given
        final TechnicalTag java = TechnicalTagFixture.create(tagName("java"));
        final TechnicalTag spring = TechnicalTagFixture.create(tagName("spring"));
        final Runner runner = RunnerFixture.createRunner(MemberFixture.createHyena(), List.of(java, spring));
        final String token = getAccessTokenBySocialId(runner.getMember().getSocialId().getValue());

        when(oauthRunnerRepository.joinByMemberSocialId(notNull()))
                .thenReturn(Optional.ofNullable(runner));

        // then
        mockMvc.perform(get("/api/v1/profile/runner/me")
                        .header(AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT")
                        ),
                        responseFields(
                                fieldWithPath("name").type(STRING).description("러너 이름"),
                                fieldWithPath("company").type(STRING).description("러너 소속 회사"),
                                fieldWithPath("imageUrl").type(STRING).description("러너 프로필 이미지 url"),
                                fieldWithPath("githubUrl").type(STRING).description("러너 깃허브 url"),
                                fieldWithPath("introduction").type(STRING).description("러너 자기소개"),
                                fieldWithPath("technicalTags").type(ARRAY).description("러너 기술 태그 목록")
                        )
                ))
                .andDo(print());
    }

    @DisplayName("러너 프로필 상세 조회 API")
    @Test
    void readRunnerProfile() throws Exception {
        // given
        final Member ethan = MemberFixture.createEthan();
        final TechnicalTag javaTag = TechnicalTagFixture.createJava();
        final TechnicalTag reactTag = TechnicalTagFixture.createReact();
        final Runner runner = RunnerFixture.createRunner(ethan, List.of(javaTag, reactTag));
        final Runner spyRunner = spy(runner);

        // when
        when(spyRunner.getId()).thenReturn(1L);
        when(runnerService.readByRunnerId(anyLong())).thenReturn(spyRunner);

        // then
        mockMvc.perform(get("/api/v1/profile/runner/{runnerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("runnerId").type(NUMBER).description("러너 식별자값"),
                                fieldWithPath("name").type(STRING).description("러너 이름"),
                                fieldWithPath("imageUrl").type(STRING).description("사용자 이미지"),
                                fieldWithPath("githubUrl").type(STRING).description("깃허브 프로필 url"),
                                fieldWithPath("introduction").type(STRING).description("소개"),
                                fieldWithPath("company").type(STRING).description("소속"),
                                fieldWithPath("technicalTags").type(ARRAY).description("기술 스택")
                        )
                ));
    }

    //    @DisplayName("러너 프로필 조회 API")
//    @Test
//    void read() throws Exception {
//        // TODO: 로그인 들어오면 작성하겠삼.
//        // given
//        final Runner runner = RunnerFixture.createRunner(MemberFixture.createHyena());
//        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
//        final Tag javaTag = TagFixture.create(tagName("자바"), tagCount(10));
//        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline, List.of(javaTag));
//        final RunnerPost spyRunnerPost = spy(runnerPost);
//
//        // when
//
//        // then
//    }
}
