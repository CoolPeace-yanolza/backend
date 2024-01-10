package com.coolpeace.docs.member;

import com.coolpeace.docs.utils.MemberTestUtil;
import com.coolpeace.domain.member.dto.request.MemberLoginRequest;
import com.coolpeace.domain.member.dto.request.MemberRegisterRequest;
import com.coolpeace.domain.member.dto.request.RefreshAccessTokenRequest;
import com.coolpeace.domain.member.dto.response.MemberLoginResponse;
import com.coolpeace.domain.member.dto.response.MemberRefreshAccessTokenResponse;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.global.common.RestDocsIntegrationTest;
import com.coolpeace.global.builder.MemberTestBuilder;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends RestDocsIntegrationTest {
    private static final String RESOURCE_TAG = "멤버";
    private static final String URL_DOMAIN_PREFIX = "/v1/member";
    private static final String BEARER_PREFIX = "Bearer ";

    @DisplayName("멤버 회원가입")
    @Test
    void memberRegister_success() throws Exception {
        // given
        Member member = new MemberTestBuilder().build();

        MemberRegisterRequest registerRequest =
                new MemberRegisterRequest(member.getEmail(), member.getPassword(), member.getName());

        // when
        ResultActions result = mockMvc.perform(post(URL_DOMAIN_PREFIX + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)));

        // then
        result.andExpect(status().isCreated())
                .andDo(document("member-register",
                        resource(ResourceSnippetParameters.builder()
                                .tag(RESOURCE_TAG)
                                .description("멤버 회원가입 API")
                                .requestSchema(Schema.schema(MemberRegisterRequest.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("멤버 이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("멤버 비밀번호"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("멤버 이름")
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("멤버 ID 중복확인")
    @Test
    void memberRegisterCheckEmail_success() throws Exception {
        // given
        Member member = new MemberTestBuilder().build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", member.getEmail());

        // when
        ResultActions result = mockMvc.perform(get(URL_DOMAIN_PREFIX + "/register/check/email")
                .params(params));

        // then
        result.andExpect(status().isOk())
                .andDo(document("member-register-email-check",
                        resource(ResourceSnippetParameters.builder()
                                .tag(RESOURCE_TAG)
                                .description("멤버 회원가입 이메일 체크 API")
                                .queryParameters(
                                        parameterWithName("email").type(SimpleType.STRING).description("가입할 이메일")
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("멤버 로그인")
    @Test
    void memberLogin_success() throws Exception {
        // given
        Member member = MemberTestUtil.registerNewTestMember(mockMvc, objectMapper);
        MemberLoginRequest request = new MemberLoginRequest(member.getEmail(), member.getPassword());

        // when
        ResultActions result = this.mockMvc.perform(post(URL_DOMAIN_PREFIX + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("member-login",
                        resource(ResourceSnippetParameters.builder()
                                .tag(RESOURCE_TAG)
                                .description("멤버 로그인 API")
                                .requestSchema(Schema.schema(MemberLoginRequest.class.getSimpleName()))
                                .responseSchema(Schema.schema(MemberLoginResponse.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("멤버 이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("멤버 비밀번호")
                                )
                                .responseFields(
                                        fieldWithPath("access_token").type(JsonFieldType.STRING).description("JWT 액세스 토큰"),
                                        fieldWithPath("refresh_token").type(JsonFieldType.STRING).description("JWT 리프레시 토큰"),
                                        fieldWithPath("expires_in").type(JsonFieldType.NUMBER).description("JWT 액세스 토큰 만료 시간(초 단위)"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일")
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("멤버 로그아웃")
    @Test
    void memberLogout_success() throws Exception {
        // given
        MemberLoginResponse loginResponse = MemberTestUtil.obtainAccessTokenOfNewTestMember(mockMvc, objectMapper);

        // when
        ResultActions result = this.mockMvc.perform(post(URL_DOMAIN_PREFIX + "/logout")
                .header(AUTHORIZATION, BEARER_PREFIX + loginResponse.accessToken())
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("member-logout",
                        resource(ResourceSnippetParameters.builder()
                                .tag(RESOURCE_TAG)
                                .description("멤버 로그아웃 API")
                                .build()
                        )
                ));
    }

    @DisplayName("멤버 액세스 토큰 리프레시")
    @Test
    void memberRefreshAccessToken_success() throws Exception {
        // given
        MemberLoginResponse loginResponse = MemberTestUtil.obtainAccessTokenOfNewTestMember(mockMvc, objectMapper);
        RefreshAccessTokenRequest request = new RefreshAccessTokenRequest(loginResponse.refreshToken());

        // when
        ResultActions result = this.mockMvc.perform(post(URL_DOMAIN_PREFIX + "/refresh")
                .header(AUTHORIZATION, BEARER_PREFIX + loginResponse.accessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("member-access-token-refresh",
                        resource(ResourceSnippetParameters.builder()
                                .tag(RESOURCE_TAG)
                                .description("멤버 액세스 토큰 리프레시 API")
                                .requestSchema(Schema.schema(RefreshAccessTokenRequest.class.getSimpleName()))
                                .responseSchema(Schema.schema(MemberRefreshAccessTokenResponse.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("refresh_token").type(JsonFieldType.STRING).description("멤버 리프레시 토큰")
                                )
                                .responseFields(
                                        fieldWithPath("access_token").type(JsonFieldType.STRING).description("JWT 액세스 토큰"),
                                        fieldWithPath("refresh_token").type(JsonFieldType.STRING).description("JWT 리프레시 토큰"),
                                        fieldWithPath("expires_in").type(JsonFieldType.NUMBER).description("JWT 액세스 토큰 만료 시간(초 단위)")
                                )
                                .build()
                        )
                ));
    }
}
