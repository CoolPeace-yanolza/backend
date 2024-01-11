package com.coolpeace.global.util;

import com.coolpeace.domain.member.dto.request.MemberLoginRequest;
import com.coolpeace.domain.member.dto.request.MemberRegisterRequest;
import com.coolpeace.domain.member.dto.response.MemberLoginResponse;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.global.builder.MemberTestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

public class MemberTestUtil {
    private static final String URL_DOMAIN_PREFIX = "/v1/member";

    public static Member registerNewTestMember(MockMvc mockMvc,
                                               ObjectMapper objectMapper) throws Exception {
        Member member = new MemberTestBuilder().build();

        MemberRegisterRequest registerRequest =
                new MemberRegisterRequest(member.getEmail(), member.getPassword(), member.getName());
        int registerStatus = mockMvc.perform(post(URL_DOMAIN_PREFIX + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn().getResponse().getStatus();

        if (registerStatus != CREATED.value()) {
            Assertions.fail("멤버 회원가입 실패 : " + registerStatus);
        }
        return member;
    }

    public static MemberLoginResponse obtainAccessTokenOfNewTestMember(MockMvc mockMvc,
                                                                       ObjectMapper objectMapper) throws Exception {
        Member member = registerNewTestMember(mockMvc, objectMapper);
        return obtainAccessTokenByTestMember(mockMvc, objectMapper, member);
    }

    public static MemberLoginResponse obtainAccessTokenByTestMember(MockMvc mockMvc,
                                                                    ObjectMapper objectMapper,
                                                                    Member member) throws Exception {
        MemberLoginRequest loginRequest = new MemberLoginRequest(member.getEmail(), member.getPassword());

        String loginResultStr = mockMvc.perform(post(URL_DOMAIN_PREFIX + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(loginResultStr, MemberLoginResponse.class);
    }
}
