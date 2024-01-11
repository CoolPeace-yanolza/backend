package com.coolpeace.docs.accommodation;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coolpeace.docs.utils.MemberTestUtil;
import com.coolpeace.domain.accommodation.dto.response.AccommodationResponse;
import com.coolpeace.domain.accommodation.dto.response.RoomResponse;
import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.member.dto.response.MemberLoginResponse;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.room.repository.RoomRepository;
import com.coolpeace.global.builder.AccommodationTestBuilder;
import com.coolpeace.global.builder.RoomTestBuilder;
import com.coolpeace.global.common.RestDocsIntegrationTest;
import com.coolpeace.global.config.RestDocsUnitTest;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;

@Transactional
public class AccommodationControllerTest extends RestDocsIntegrationTest {

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;
    @Autowired
    private RoomRepository roomRepository;

    private Member registeredMember;
    private Member storedMember;
    private Accommodation accommodation;
    private List<Room> rooms;
    @BeforeEach
    void beforeEach() throws Exception {
        // 로그인 (영속화)
        this.registeredMember = MemberTestUtil.registerNewTestMember(mockMvc, objectMapper);
        this.storedMember = memberRepository.findByEmail(registeredMember.getEmail())
            .orElseThrow(MemberNotFoundException::new);

        // 숙박 및 객실 저장 (영속화)
        Accommodation mockAccommodation = new AccommodationTestBuilder(this.storedMember).build();
        this.accommodation = accommodationRepository.save(mockAccommodation);
        this.rooms = new RoomTestBuilder(this.accommodation).buildList();
        roomRepository.saveAll(this.rooms);
    }
    
    @DisplayName("숙소 목록 조회")
    @Test
    void selectAccommodations() throws Exception {

        //given
        MemberLoginResponse loginResponse = MemberTestUtil
            .obtainAccessTokenByTestMember(mockMvc, objectMapper, registeredMember);


        //when
        ResultActions result = mockMvc.perform(get("/v1/accommodation")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, BEARER_PREFIX + loginResponse.accessToken()));


        //then
        result
            .andExpect(status().isOk())
            .andDo(document("accommodation-search",
                resource(ResourceSnippetParameters.builder()
                    .tag("숙소")
                    .description("숙소 목록 조회 API")
                    .responseSchema(Schema.schema(AccommodationResponse.class.getSimpleName()))
                    .responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("숙소 ID"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("숙소명"),
                        fieldWithPath("[].sido_id").type(JsonFieldType.NUMBER).description("시도ID"),
                        fieldWithPath("[].sido").type(JsonFieldType.STRING).description("시도명"),
                        fieldWithPath("[].sigungu_id").type(JsonFieldType.NUMBER).description("시군구ID"),
                        fieldWithPath("[].sigungu").type(JsonFieldType.STRING).description("시군구명"),
                        fieldWithPath("[].address").type(JsonFieldType.STRING).description("숙소 상세")
                    )
                    .build()
                )
                ));
    }

    @DisplayName("숙소의 객실 조회")
    @Test
    void selectAccommodationRooms() throws Exception {

        //given
        MemberLoginResponse loginResponse = MemberTestUtil
            .obtainAccessTokenByTestMember(mockMvc, objectMapper, registeredMember);

        //when
        ResultActions result = mockMvc.perform(get("/v1/accommodation/{accommodation_id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, BEARER_PREFIX + loginResponse.accessToken()));

        //then
        result
            .andExpect(status().isOk())
            .andDo(document("room-search",
                resource(ResourceSnippetParameters.builder()
                    .tag("숙소")
                    .description("숙소의 객실 목록 조회 API")
                    .responseSchema(Schema.schema(RoomResponse.class.getSimpleName()))
                    .responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("방ID"),
                        fieldWithPath("[].room_number").type(JsonFieldType.NUMBER).description("방번호"),
                        fieldWithPath("[].room_type").type(JsonFieldType.STRING).description("방타입"),
                        fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("가격")
                    )
                    .build()
            )));

    }



}
