package com.coolpeace.docs.coupon;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.dto.request.CouponExposeRequest;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.domain.coupon.dto.request.type.SearchCouponDateFilterType;
import com.coolpeace.domain.coupon.dto.request.type.SearchCouponStatusFilterType;
import com.coolpeace.domain.coupon.dto.response.CouponResponse;
import com.coolpeace.domain.coupon.dto.response.CouponSearchResponse;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponIssuerType;
import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import com.coolpeace.domain.member.dto.response.MemberLoginResponse;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.room.repository.RoomRepository;
import com.coolpeace.global.builder.AccommodationTestBuilder;
import com.coolpeace.global.builder.CouponTestBuilder;
import com.coolpeace.global.builder.RoomTestBuilder;
import com.coolpeace.global.common.DayOfWeekUtil;
import com.coolpeace.global.common.RestDocsIntegrationTest;
import com.coolpeace.global.util.CouponTestUtil;
import com.coolpeace.global.util.MemberTestUtil;
import com.coolpeace.global.util.RoomTestUtil;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class CouponControllerTest extends RestDocsIntegrationTest {
    private static final String RESOURCE_TAG = "쿠폰";
    private static final String URL_DOMAIN_PREFIX = "/v1/coupons";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CouponRepository couponRepository;

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

    @DisplayName("쿠폰 등록")
    @Test
    void registerCoupon_success() throws Exception {
        // given
        MemberLoginResponse loginResponse = MemberTestUtil
                .obtainAccessTokenByTestMember(mockMvc, objectMapper, registeredMember);

        List<Room> randomRooms = RoomTestUtil.getRandomRooms(rooms);
        List<Integer> randomRoomNumbers = rooms.stream().map(Room::getRoomNumber).toList();
        Coupon coupon = new CouponTestBuilder(accommodation, storedMember, randomRooms).build();

        CouponRegisterRequest couponRegisterRequest = new CouponRegisterRequest(
                coupon.getTitle(),
                coupon.getCustomerType().getValue(),
                coupon.getDiscountType().getValue(),
                coupon.getDiscountValue(),
                coupon.getCouponRoomType().getValue(),
                accommodation.getId(),
                false,
                randomRoomNumbers,
                coupon.getMinimumReservationPrice(),
                DayOfWeekUtil.fromDayOfWeeks(coupon.getCouponUseConditionDays()),
                coupon.getExposureStartDate(),
                coupon.getExposureEndDate()
        );

        // when
        ResultActions result = mockMvc.perform(post(URL_DOMAIN_PREFIX + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, BEARER_PREFIX + loginResponse.accessToken())
                .content(objectMapper.writeValueAsString(couponRegisterRequest)));

        // then
        result
//                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("coupon-register",
                        resource(ResourceSnippetParameters.builder()
                                .tag(RESOURCE_TAG)
                                .description("쿠폰 등록 API")
                                .requestSchema(Schema.schema(CouponRegisterRequest.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("쿠폰의 이름"),
                                        fieldWithPath("customer_type").type(JsonFieldType.STRING).description("고객의 유형"),
                                        fieldWithPath("discount_type").type(JsonFieldType.STRING).description("할인의 유형"),
                                        fieldWithPath("discount_value").type(JsonFieldType.NUMBER).description("할인의 값"),
                                        fieldWithPath("coupon_room_type").type(JsonFieldType.STRING).description("객실의 유형"),
                                        fieldWithPath("accommodation_id").type(JsonFieldType.NUMBER).description("숙박업체의 ID"),
                                        fieldWithPath("register_all_room").type(JsonFieldType.BOOLEAN).description("객실 등록 여부"),
                                        fieldWithPath("register_rooms").type(JsonFieldType.ARRAY).description("등록될 객실의 리스트"),
                                        fieldWithPath("minimum_reservation_price").type(JsonFieldType.NUMBER).description("최소 예약 가격"),
                                        fieldWithPath("coupon_use_condition_days").type(JsonFieldType.ARRAY).description("쿠폰 사용 가능 요일"),
                                        fieldWithPath("exposure_start_date").type(JsonFieldType.STRING).description("노출 시작 날짜"),
                                        fieldWithPath("exposure_end_date").type(JsonFieldType.STRING).description("노출 종료 날짜")
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("쿠폰 검색")
    @Test
    void searchCoupon_success() throws Exception {
        // given
        MemberLoginResponse loginResponse = MemberTestUtil
                .obtainAccessTokenByTestMember(mockMvc, objectMapper, registeredMember);

        for (int i = 0; i < 10; i++) {
            List<Room> randomRooms;
            if (i % 3 == 0) {
                randomRooms = RoomTestUtil.getRandomRooms(this.rooms);
            } else {
                randomRooms = Collections.emptyList();
            }
            Coupon coupon = couponRepository.save(new CouponTestBuilder(accommodation, storedMember, randomRooms).build());
            coupon.generateCouponNumber(CouponIssuerType.OWNER, coupon.getId());
        }

        // when
        MultiValueMap<String, String> requestParams = createCouponSearchParams();
        ResultActions result = mockMvc.perform(get(URL_DOMAIN_PREFIX)
                .header(AUTHORIZATION, BEARER_PREFIX + loginResponse.accessToken())
                .queryParams(requestParams));

        // then
        result
//                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("coupon-search",
                        resource(ResourceSnippetParameters.builder()
                                .tag(RESOURCE_TAG)
                                .description("쿠폰 검색 API")
                                .queryParameters(
                                        parameterWithName("status").type(SimpleType.STRING).description("쿠폰 상태").optional(),
                                        parameterWithName("title").type(SimpleType.STRING).description("쿠폰 이름").optional(),
                                        parameterWithName("date").type(SimpleType.STRING).description("쿠폰 날짜").optional(),
                                        parameterWithName("page").type(SimpleType.STRING).description("페이지 번호(1번부터 시작)").optional(),
                                        parameterWithName("size").type(SimpleType.STRING).description("페이지 크기").optional()
                                )
                                .responseSchema(Schema.schema(CouponSearchResponse.class.getSimpleName()))
                                .responseFields(
                                        fieldWithPath("content[].title").type(JsonFieldType.STRING).description("쿠폰의 이름"),
                                        fieldWithPath("content[].coupon_number").type(JsonFieldType.STRING).description("쿠폰 번호"),
                                        fieldWithPath("content[].coupon_status").type(JsonFieldType.STRING).description("쿠폰 상태"),
                                        fieldWithPath("content[].coupon_promotion").type(JsonFieldType.BOOLEAN).description("쿠폰 프로모션 여부"),
                                        fieldWithPath("content[].coupon_concat_title").type(JsonFieldType.STRING).description("고객 유형 + 할인 유형 + 할인의 값"),
                                        fieldWithPath("content[].discount_type").type(JsonFieldType.STRING).description("할인의 유형"),
                                        fieldWithPath("content[].discount_value").type(JsonFieldType.NUMBER).description("할인의 값"),
                                        fieldWithPath("content[].customer_type").type(JsonFieldType.STRING).description("고객의 유형"),
                                        fieldWithPath("content[].coupon_room_type").type(JsonFieldType.STRING).description("객실의 유형"),
                                        fieldWithPath("content[].minimum_reservation_price").type(JsonFieldType.NUMBER).description("최소 예약 가격"),
                                        fieldWithPath("content[].coupon_use_condition_days").type(JsonFieldType.ARRAY).description("쿠폰 사용 가능 요일"),
                                        fieldWithPath("content[].exposure_start_date").type(JsonFieldType.STRING).description("노출 시작 날짜"),
                                        fieldWithPath("content[].exposure_end_date").type(JsonFieldType.STRING).description("노출 종료 날짜"),
                                        fieldWithPath("content[].coupon_expiration").type(JsonFieldType.NUMBER).description("쿠폰 만료 일자"),
                                        fieldWithPath("content[].download_count").type(JsonFieldType.NUMBER).description("다운로드 횟수"),
                                        fieldWithPath("content[].use_count").type(JsonFieldType.NUMBER).description("사용 수"),
                                        fieldWithPath("content[].accommodation_id").type(JsonFieldType.NUMBER).description("숙박업체의 ID"),
                                        fieldWithPath("content[].register_room_numbers").type(JsonFieldType.ARRAY).description("등록된 객실 번호"),
                                        fieldWithPath("content[].created_date").type(JsonFieldType.STRING).description("생성 날짜"),
                                        fieldWithPath("category.all").type(JsonFieldType.NUMBER).description("쿠폰 카테고리 - 전체"),
                                        fieldWithPath("category.exposure_on").type(JsonFieldType.NUMBER).description("쿠폰 카테고리 - 노출 ON"),
                                        fieldWithPath("category.exposure_off").type(JsonFieldType.NUMBER).description("쿠폰 카테고리 - 노출 OFF"),
                                        fieldWithPath("category.expiration").type(JsonFieldType.NUMBER).description("쿠폰 카테고리 - 만료"),
                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("total_elements").type(JsonFieldType.NUMBER).description("총 엘리먼트 수"),
                                        fieldWithPath("total_pages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                        fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지당 사이즈"),
                                        fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫번째 페이지 여부"),
                                        fieldWithPath("number_of_elements").type(JsonFieldType.NUMBER).description("현재 페이지 엘리먼트 수"),
                                        fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("페이지 내용이 비어있는지 여부")
                                )
                                .build()
                        )
                ));
    }

    private static MultiValueMap<String, String> createCouponSearchParams() {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("status", SearchCouponStatusFilterType.All.getValue());
//        requestParams.add("title", "");
        requestParams.add("date", SearchCouponDateFilterType.YEAR.getValue());
        requestParams.add("page", "1");
        return requestParams;
    }

    @DisplayName("이전 쿠폰 등록 내역 조회")
    @Nested
    class GetCouponRecentHistoryTest {
        @DisplayName("이전 쿠폰 등록 내역 조회 - 조회 성공")
        @Test
        void getCouponRecentHistory_success() throws Exception {
            // given
            MemberLoginResponse loginResponse = MemberTestUtil
                    .obtainAccessTokenByTestMember(mockMvc, objectMapper, registeredMember);

            List<Room> randomRooms = RoomTestUtil.getRandomRooms(rooms);
            couponRepository.saveAll(CouponTestUtil.getTestCoupons(accommodation, storedMember, randomRooms));

            // when
            ResultActions result = mockMvc.perform(get(URL_DOMAIN_PREFIX + "/recent")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, BEARER_PREFIX + loginResponse.accessToken()));

            // then
            result.andExpect(status().is2xxSuccessful());
            result.andDo(document("coupon-recent-history",
                    resource(ResourceSnippetParameters.builder()
                            .tag(RESOURCE_TAG)
                            .description("이전 쿠폰 등록 내역 조회 API")
                            .responseSchema(Schema.schema(CouponResponse.class.getSimpleName()))
                            .responseFields(
                                    fieldWithPath("[].title").type(JsonFieldType.STRING).description("쿠폰의 이름"),
                                    fieldWithPath("[].coupon_number").type(JsonFieldType.STRING).description("쿠폰 번호"),
                                    fieldWithPath("[].coupon_status").type(JsonFieldType.STRING).description("쿠폰 상태"),
                                    fieldWithPath("[].coupon_promotion").type(JsonFieldType.BOOLEAN).description("쿠폰 프로모션 여부"),
                                    fieldWithPath("[].coupon_concat_title").type(JsonFieldType.STRING).description("고객 유형 + 할인 유형 + 할인의 값"),
                                    fieldWithPath("[].discount_type").type(JsonFieldType.STRING).description("할인의 유형"),
                                    fieldWithPath("[].discount_value").type(JsonFieldType.NUMBER).description("할인의 값"),
                                    fieldWithPath("[].customer_type").type(JsonFieldType.STRING).description("고객의 유형"),
                                    fieldWithPath("[].coupon_room_type").type(JsonFieldType.STRING).description("객실의 유형"),
                                    fieldWithPath("[].minimum_reservation_price").type(JsonFieldType.NUMBER).description("최소 예약 가격"),
                                    fieldWithPath("[].coupon_use_condition_days").type(JsonFieldType.ARRAY).description("쿠폰 사용 가능 요일"),
                                    fieldWithPath("[].exposure_start_date").type(JsonFieldType.STRING).description("노출 시작 날짜"),
                                    fieldWithPath("[].exposure_end_date").type(JsonFieldType.STRING).description("노출 종료 날짜"),
                                    fieldWithPath("[].coupon_expiration").type(JsonFieldType.NUMBER).description("쿠폰 만료 일자"),
                                    fieldWithPath("[].download_count").type(JsonFieldType.NUMBER).description("다운로드 횟수"),
                                    fieldWithPath("[].use_count").type(JsonFieldType.NUMBER).description("사용 수"),
                                    fieldWithPath("[].accommodation_id").type(JsonFieldType.NUMBER).description("숙박업체의 ID"),
                                    fieldWithPath("[].register_room_numbers").type(JsonFieldType.ARRAY).description("등록된 객실 번호"),
                                    fieldWithPath("[].created_date").type(JsonFieldType.STRING).description("생성 날짜")
                            )
                            .build()
                    )
            ));
        }

        @DisplayName("이전 쿠폰 등록 내역 조회 - 조회 결과 없음")
        @Test
        void getCouponRecentHistory_success_no_content() throws Exception {
            // given
            MemberLoginResponse loginResponse = MemberTestUtil
                    .obtainAccessTokenByTestMember(mockMvc, objectMapper, registeredMember);

            // when
            ResultActions result = mockMvc.perform(get(URL_DOMAIN_PREFIX + "/recent")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, BEARER_PREFIX + loginResponse.accessToken()));

            // then
            result
                    .andExpect(status().isNoContent())
                    .andDo(document("coupon-recent-history-no-content",
                            resource(ResourceSnippetParameters.builder()
                                    .tag(RESOURCE_TAG)
                                    .description("이전 쿠폰 등록 내역 조회 API")
                                    .build()
                            )
                    ));
        }
    }

    @DisplayName("쿠폰 수정")
    @Test
    void updateCoupon_success() throws Exception {
        // given
        MemberLoginResponse loginResponse = MemberTestUtil
                .obtainAccessTokenByTestMember(mockMvc, objectMapper, registeredMember);

        List<Room> randomRooms = RoomTestUtil.getRandomRooms(rooms);
        Coupon coupon = couponRepository.save(new CouponTestBuilder(accommodation, storedMember, randomRooms).build());
        coupon.generateCouponNumber(CouponIssuerType.OWNER, coupon.getId());
        CouponUpdateRequest request = new CouponUpdateRequest(
                accommodation.getId(),
                coupon.getCustomerType().getValue(),
                coupon.getDiscountType().getValue(),
                coupon.getDiscountValue(),
                coupon.getCouponRoomType().getValue(),
                coupon.getCouponRooms().isEmpty(),
                coupon.getCouponRooms().stream().map(room -> room.getRoom().getRoomNumber()).toList(),
                coupon.getMinimumReservationPrice() + 10000,
                DayOfWeekUtil.fromDayOfWeeks(coupon.getCouponUseConditionDays()),
                coupon.getExposureStartDate(),
                coupon.getExposureEndDate()
        );

        // when
        ResultActions result = mockMvc.perform(put(URL_DOMAIN_PREFIX + "/{coupon_number}", coupon.getCouponNumber())
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, BEARER_PREFIX + loginResponse.accessToken())
                .content(objectMapper.writeValueAsString(request))
        );
        // then
        result
//                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("coupon-update",
                        resource(ResourceSnippetParameters.builder()
                                .tag(RESOURCE_TAG)
                                .description("쿠폰 수정 API")
                                .requestSchema(Schema.schema(CouponUpdateRequest.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("accommodation_id").type(JsonFieldType.NUMBER).description("숙박업체의 ID"),
                                        fieldWithPath("customer_type").type(JsonFieldType.STRING).description("고객의 유형").optional(),
                                        fieldWithPath("discount_type").type(JsonFieldType.STRING).description("할인의 유형").optional(),
                                        fieldWithPath("discount_value").type(JsonFieldType.NUMBER).description("할인의 값").optional(),
                                        fieldWithPath("coupon_room_type").type(JsonFieldType.STRING).description("객실의 유형").optional(),
                                        fieldWithPath("register_all_room").type(JsonFieldType.BOOLEAN).description("객실 등록 여부").optional(),
                                        fieldWithPath("register_rooms").type(JsonFieldType.ARRAY).description("등록될 객실의 리스트").optional(),
                                        fieldWithPath("minimum_reservation_price").type(JsonFieldType.NUMBER).description("최소 예약 가격").optional(),
                                        fieldWithPath("coupon_use_condition_days").type(JsonFieldType.ARRAY).description("쿠폰 사용 가능 요일").optional(),
                                        fieldWithPath("exposure_start_date").type(JsonFieldType.STRING).description("노출 시작 날짜").optional(),
                                        fieldWithPath("exposure_end_date").type(JsonFieldType.STRING).description("노출 종료 날짜").optional()
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("쿠폰 노출 여부 수정")
    @Test
    void exposeCoupon_success() throws Exception {
        // given
        MemberLoginResponse loginResponse = MemberTestUtil
                .obtainAccessTokenByTestMember(mockMvc, objectMapper, registeredMember);

        List<Room> randomRooms = RoomTestUtil.getRandomRooms(rooms);
        Coupon coupon = couponRepository.save(new CouponTestBuilder(accommodation, storedMember, randomRooms)
                .currentExposureDates().build());
        coupon.generateCouponNumber(CouponIssuerType.OWNER, coupon.getId());
        CouponExposeRequest request = new CouponExposeRequest(CouponStatusType.EXPOSURE_OFF.getValue());

        // when
        ResultActions result = mockMvc.perform(put(URL_DOMAIN_PREFIX + "/{coupon_number}/expose", coupon.getCouponNumber())
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, BEARER_PREFIX + loginResponse.accessToken())
                .content(objectMapper.writeValueAsString(request))
        );
        // then
        result
//                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("coupon-expose",
                        resource(ResourceSnippetParameters.builder()
                                .tag(RESOURCE_TAG)
                                .description("쿠폰 노출 여부 수정 API")
                                .requestSchema(Schema.schema(CouponExposeRequest.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("coupon_status").type(JsonFieldType.STRING).description("쿠폰 상태")
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("쿠폰 삭제")
    @Test
    void deleteCoupon_success() throws Exception {
        // given
        MemberLoginResponse loginResponse = MemberTestUtil
                .obtainAccessTokenByTestMember(mockMvc, objectMapper, registeredMember);

        List<Room> randomRooms = RoomTestUtil.getRandomRooms(rooms);
        Coupon coupon = couponRepository.save(new CouponTestBuilder(accommodation, storedMember, randomRooms).build());
        coupon.generateCouponNumber(CouponIssuerType.OWNER, coupon.getId());

        // when
        ResultActions result = mockMvc.perform(delete(URL_DOMAIN_PREFIX + "/{coupon_number}", coupon.getCouponNumber())
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, BEARER_PREFIX + loginResponse.accessToken())
        );
        // then
        result
//                    .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("coupon-delete",
                        resource(ResourceSnippetParameters.builder()
                                .tag(RESOURCE_TAG)
                                .description("쿠폰 삭제 API")
                                .build()
                        )
                ));
    }
}
