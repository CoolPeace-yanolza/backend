package com.coolpeace.docs.coupon;

import com.coolpeace.docs.utils.AccommodationTestUtil;
import com.coolpeace.docs.utils.MemberTestUtil;
import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.member.dto.response.MemberLoginResponse;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.room.repository.RoomRepository;
import com.coolpeace.global.builder.AccommodationTestBuilder;
import com.coolpeace.global.builder.CouponTestBuilder;
import com.coolpeace.global.builder.RoomTestBuilder;
import com.coolpeace.global.common.RestDocsIntegrationTest;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @DisplayName("쿠폰 등록")
    @Test
    void registerCoupon_success() throws Exception {
        // given
        // 로그인
        Member member = MemberTestUtil.registerNewTestMember(mockMvc, objectMapper);
        Member savedMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        MemberLoginResponse loginResponse = MemberTestUtil.obtainAccessTokenByTestMember(mockMvc, objectMapper, member);

        // 숙박 및 객실 저장
        Accommodation accommodation = new AccommodationTestBuilder(savedMember).build();
        Accommodation savedAccommodation = accommodationRepository.save(accommodation);
        List<Room> rooms = new RoomTestBuilder(savedAccommodation).buildList();
        roomRepository.saveAll(rooms);

        // 쿠폰 객체 생성
        List<Room> randomRooms = AccommodationTestUtil.getRandomRooms(rooms);
        List<Integer> randomRoomNumbers = rooms.stream().map(Room::getRoomNumber).toList();
        Coupon coupon = new CouponTestBuilder(accommodation, member, randomRooms).build();

        CouponRegisterRequest couponRegisterRequest = new CouponRegisterRequest(
                coupon.getTitle(),
                coupon.getCustomerType(),
                coupon.getDiscountType(),
                coupon.getDiscountValue(),
                coupon.getCouponRoomType(),
                accommodation.getId(),
                false,
                randomRoomNumbers,
                coupon.getMinimumReservationPrice(),
                coupon.getCouponUseConditionDays(),
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
                .andDo(print())
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
}
