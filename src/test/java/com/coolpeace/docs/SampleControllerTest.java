package com.coolpeace.docs;

import com.coolpeace.domain.member.dto.request.MemberRegisterRequest;
import com.coolpeace.global.config.RestDocsIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SampleControllerTest extends RestDocsIntegrationTest {
    @Disabled
    @Test
    public void test() throws Exception {
        // given
        MemberRegisterRequest request = new MemberRegisterRequest("test@test.com", "pass111", "test123");

        // when
        ResultActions result = this.mockMvc.perform(post("/v1/member/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isCreated())
                .andDo(document("/v1/member/register",
                        // 모든 필드가 들어있으면 requestFields, 일부 필드만 적으려면 relaxedRequestFields
                        requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                                )
                        )
                );
    }

}
