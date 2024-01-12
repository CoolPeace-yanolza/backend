package com.coolpeace.api.docs.accommodation;

import com.coolpeace.api.domain.accommodation.dto.response.AccommodationResponse;
import com.coolpeace.api.domain.accommodation.dto.response.RoomResponse;
import com.coolpeace.api.domain.member.dto.response.MemberLoginResponse;
import com.coolpeace.api.domain.member.exception.MemberNotFoundException;
import com.coolpeace.api.global.builder.AccommodationTestBuilder;
import com.coolpeace.api.global.builder.RoomTestBuilder;
import com.coolpeace.api.global.common.RestDocsIntegrationTest;
import com.coolpeace.api.global.util.MemberTestUtil;
import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.member.repository.MemberRepository;
import com.coolpeace.core.domain.room.entity.Room;
import com.coolpeace.core.domain.room.repository.RoomRepository;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
