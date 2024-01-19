package com.coolpeace.domain.coupon.service;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.dto.request.CouponExposeRequest;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.dto.request.type.SearchCouponDateFilterType;
import com.coolpeace.domain.coupon.dto.request.type.SearchCouponStatusFilterType;
import com.coolpeace.domain.coupon.dto.response.CouponResponse;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.*;
import com.coolpeace.domain.coupon.exception.*;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.room.exception.RegisterRoomsEmptyException;
import com.coolpeace.domain.room.exception.RoomNotFoundException;
import com.coolpeace.domain.room.repository.RoomRepository;
import com.coolpeace.global.builder.AccommodationTestBuilder;
import com.coolpeace.global.builder.CouponTestBuilder;
import com.coolpeace.global.builder.MemberTestBuilder;
import com.coolpeace.global.builder.RoomTestBuilder;
import com.coolpeace.global.common.DayOfWeekUtil;
import com.coolpeace.global.util.CouponTestUtil;
import com.coolpeace.global.util.RoomTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Transactional
@ExtendWith(MockitoExtension.class)
class CouponServiceTest {
    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private RoomRepository roomRepository;

    private Member member;
    private Accommodation accommodation;
    private List<Room> rooms;

    @BeforeEach
    void beforeEach() {
        this.member = new MemberTestBuilder().encoded().build();
        this.accommodation = new AccommodationTestBuilder(member).build();
        this.rooms = new RoomTestBuilder(accommodation).buildList();

        ReflectionTestUtils.setField(member, "id", 101L);
        ReflectionTestUtils.setField(accommodation, "id", 1234L);
    }

    @DisplayName("쿠폰 조회")
    @Nested
    class CouponSearchTest {
        private List<Coupon> coupons;

        @BeforeEach
        void beforeEach() {
            coupons = CouponTestUtil.getTestCoupons(accommodation, member, rooms);
        }

        @DisplayName("필터 없이 쿠폰 조회를 할 수 있다.")
        @Test
        void no_filter_success() {
            // given
            SearchCouponParams searchCouponParams = new SearchCouponParams(null, null, null);
            given(couponRepository.findAllCoupons(anyLong(), any(SearchCouponParams.class), any(PageRequest.class)))
                    .willReturn(new PageImpl<>(coupons));

            // when
            Page<CouponResponse> result = couponService.searchCoupons(member.getId(), searchCouponParams, Pageable.ofSize(10));

            // then
            verify(couponRepository).findAllCoupons(anyLong(), any(SearchCouponParams.class), any(PageRequest.class));

            assertThat(result).isNotNull();
        }

        private static List<SearchCouponParams> searchFilterMethodSource() {
            List<SearchCouponParams> params = new ArrayList<>();
            List<String> titles = Arrays.asList("", null, "testTitle");
            for (SearchCouponStatusFilterType statusType : SearchCouponStatusFilterType.values()) {
                for (SearchCouponDateFilterType dateType : SearchCouponDateFilterType.values()) {
                    for (String title : titles) {
                        params.add(new SearchCouponParams(statusType.getValue(), title, dateType.getValue()));
                    }
                }
            }
            return params;
        }

        @DisplayName("필터를 통해 쿠폰 조회를 할 수 있다.")
        @ParameterizedTest
        @MethodSource("searchFilterMethodSource")
        void filtered_search_success(SearchCouponParams params) {
            // given
            given(couponRepository.findAllCoupons(anyLong(), any(SearchCouponParams.class), any(PageRequest.class)))
                    .willReturn(new PageImpl<>(coupons));

            // when
            Page<CouponResponse> result = couponService.searchCoupons(member.getId(), params, Pageable.ofSize(10));

            // then
            verify(couponRepository).findAllCoupons(anyLong(), any(SearchCouponParams.class), any(PageRequest.class));

            assertThat(result).isNotNull();
        }

        @DisplayName("페이지네이션과 필터를 통해 쿠폰 조회를 할 수 있다.")
        @Test
        void paged_filtered_search_success() {
            // given
            SearchCouponParams searchCouponParams = new SearchCouponParams(null, null, SearchCouponDateFilterType.YEAR.getValue());
            PageRequest firstPageRequest = PageRequest.of(0, 10);

            given(couponRepository.findAllCoupons(anyLong(), any(SearchCouponParams.class), any(PageRequest.class)))
                    .willReturn(new PageImpl<>(coupons.subList(0, 10)));

            // when
            Page<CouponResponse> firstPageResult = couponService.searchCoupons(member.getId(), searchCouponParams, firstPageRequest);

            // then
            verify(couponRepository).findAllCoupons(anyLong(), any(SearchCouponParams.class), any(PageRequest.class));
            assertThat(firstPageResult).isNotNull();
            assertThat(firstPageResult.getContent().size()).isEqualTo(10);
        }
    }

    @DisplayName("이전 쿠폰 등록 내역 조회")
    @Nested
    class CouponRecentHistoryTest {
        private List<Coupon> coupons;

        @BeforeEach
        void beforeEach() {
            coupons = CouponTestUtil.getOnlyExpiredTestCoupons(accommodation, member, rooms);
        }

        @DisplayName("이전 쿠폰 등록 내역을 조회할 수 있다.")
        @Test
        void getRecentHistory_success() {
            // given
            given(couponRepository.findRecentCouponByMemberId(anyLong())).willReturn(
                    coupons.stream().limit(6).toList());

            // when
            List<CouponResponse> result = couponService.getRecentHistory(member.getId());

            // then
            verify(couponRepository).findRecentCouponByMemberId(anyLong());

            assertThat(result).isNotNull();
            Predicate<CouponResponse> getNotExpiredCoupons = couponResponse -> !couponResponse.couponStatus().equals(CouponStatusType.EXPOSURE_END.getValue());
            assertThat(result.stream().filter(getNotExpiredCoupons).toList().isEmpty()).isTrue();
        }

        @DisplayName("이전 쿠폰 등록 내역이 없으면 빈 리스트를 반환한다.")
        @Test
        void getRecentHistory_empty_success() {
            // given
            given(couponRepository.findRecentCouponByMemberId(anyLong())).willReturn(
                    coupons.stream().limit(0).toList());

            // when
            List<CouponResponse> result = couponService.getRecentHistory(member.getId());

            // then
            verify(couponRepository).findRecentCouponByMemberId(anyLong());

            assertThat(result).isEmpty();
        }
    }

    @DisplayName("쿠폰 등록")
    @Nested
    class CouponRegisterTest {
        private List<Integer> registerRoomNumbers;
        private Coupon coupon;

        @BeforeEach
        void beforeEach() {
            List<Room> registerRooms = RoomTestUtil.getRandomRooms(rooms);
            this.registerRoomNumbers = registerRooms.stream().map(Room::getRoomNumber).toList();

            coupon = new CouponTestBuilder(accommodation, member, registerRooms).build();
            ReflectionTestUtils.setField(coupon, "id", 4321L);
        }

        @DisplayName("올바른 요청으로 쿠폰을 등록할 수 있다.")
        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void _success(boolean registerAllRoom) {
            // given
            CouponRegisterRequest request = getCouponRegisterRequest(registerAllRoom);

            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            given(accommodationRepository.findById(anyLong())).willReturn(Optional.of(accommodation));
            if (!registerAllRoom) {
                given(roomRepository.findByRoomNumber(anyInt())).willReturn(Optional.of(rooms.get(0)));
            }
            given(couponRepository.save(any(Coupon.class))).willReturn(coupon);

            // when
            couponService.register(member.getId(), request);

            // then
            verify(couponRepository).save(any(Coupon.class));
            assertThat(coupon.getCouponNumber()).isNotNull();
        }

        @DisplayName("멤버 ID에 해당하는 멤버가 존재하지 않는다면 쿠폰을 등록할 수 없다.")
        @Test
        void member_not_found_fail() {
            // given
            CouponRegisterRequest request = getCouponRegisterRequest();
            given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            // then
            assertThatThrownBy(() -> couponService.register(member.getId(), request))
                    .isInstanceOf(MemberNotFoundException.class);
        }

        @DisplayName("숙박 ID에 해당하는 숙박이 존재하지 않는다면 쿠폰을 등록할 수 없다.")
        @Test
        void accommodation_not_found_fail() {
            // given
            CouponRegisterRequest request = getCouponRegisterRequest();
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            given(accommodationRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            // then
            assertThatThrownBy(() -> couponService.register(member.getId(), request))
                    .isInstanceOf(AccommodationNotFoundException.class);
        }

        @DisplayName("registerAllRoom이 false지만 registerRooms가 비어 있거나 null이면 쿠폰을 등록할 수 없다.")
        @Test
        void registerAllRoom_false_but_registerRooms_is_blank_fail() {
            // given
            this.registerRoomNumbers = Collections.emptyList();
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            given(accommodationRepository.findById(anyLong())).willReturn(Optional.of(accommodation));
            CouponRegisterRequest request = getCouponRegisterRequest(false);

            // when
            // then
            assertThatThrownBy(() -> couponService.register(member.getId(), request))
                    .isInstanceOf(RegisterRoomsEmptyException.class);
        }

        @DisplayName("egisterAllRoom이 false지만 방 번호가 존재하지 않으면 쿠폰을 등록할 수 없다.")
        @Test
        void registerAllRoom_false_but_room_number_not_found_fail() {
            // given
            CouponRegisterRequest request = getCouponRegisterRequest(false);
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            given(accommodationRepository.findById(anyLong())).willReturn(Optional.of(accommodation));
            given(roomRepository.findByRoomNumber(anyInt())).willReturn(Optional.empty());

            // when
            // then
            assertThatThrownBy(() -> couponService.register(member.getId(), request))
                    .isInstanceOf(RoomNotFoundException.class);
        }

        private CouponRegisterRequest getCouponRegisterRequest() {
            return getCouponRegisterRequest(true);
        }

        private CouponRegisterRequest getCouponRegisterRequest(boolean registerAllRoom) {
            return new CouponRegisterRequest(
                    coupon.getTitle(),
                    coupon.getCustomerType().getValue(),
                    coupon.getDiscountType().getValue(),
                    coupon.getDiscountValue(),
                    coupon.getCouponRoomType().getValue(),
                    accommodation.getId(),
                    registerAllRoom,
                    !registerAllRoom ? registerRoomNumbers : null,
                    coupon.getMinimumReservationPrice(),
                    DayOfWeekUtil.fromDayOfWeeks(coupon.getCouponUseConditionDays()),
                    coupon.getExposureStartDate(),
                    coupon.getExposureEndDate()
            );
        }
    }

    @DisplayName("쿠폰 수정")
    @Nested
    class CouponUpdateTest {
        private Coupon couponA, couponB;

        @BeforeEach
        void beforeEach() {
            this.couponA = Coupon.from(
                    "titleA",
                    DiscountType.FIXED_PRICE,
                    10000,
                    CustomerType.ALL_CLIENT,
                    CouponRoomType.LODGE,
                    0,
                    Collections.emptyList(),
                    LocalDate.now().plusDays(100),
                    LocalDate.now().plusDays(130),
                    accommodation,
                    Collections.emptyList(),
                    member
            );
            ReflectionTestUtils.setField(couponA, "id", 1234L);
            couponA.generateCouponNumber(CouponIssuerType.OWNER, couponA.getId());

            this.couponB = Coupon.from(
                    "titleB",
                    DiscountType.FIXED_RATE,
                    20,
                    CustomerType.FIRST_CLIENT,
                    CouponRoomType.RENTAL,
                    1000,
                    List.of(DayOfWeek.MONDAY),
                    LocalDate.now().plusDays(30),
                    LocalDate.now().plusDays(60),
                    accommodation,
                    Collections.emptyList(),
                    member
            );
            ReflectionTestUtils.setField(couponB, "id", 1234L);
            couponB.generateCouponNumber(CouponIssuerType.OWNER, couponB.getId());
        }
        
        @DisplayName("단일 변경 - 올바른 요청으로 쿠폰을 수정할 수 있다.")
        @Test
        void updateCoupon_mono_update_success() {

            //when
            executeUpdateCoupon(new CouponUpdateRequest(accommodation.getId(),
                    couponB.getCustomerType().getValue(),
                    null,
                    null,
                    couponB.getCouponRoomType().getValue(),
                    null,
                    null,
                    couponB.getMinimumReservationPrice(),
                    DayOfWeekUtil.fromDayOfWeeks(couponB.getCouponUseConditionDays()),
                    couponB.getExposureStartDate(),
                    couponB.getExposureEndDate()
            ));

            //then
            assertThat(couponA.getCustomerType()).isEqualTo(couponB.getCustomerType());
            assertThat(couponA.getCouponRoomType()).isEqualTo(couponB.getCouponRoomType());
            assertThat(couponA.getMinimumReservationPrice()).isEqualTo(couponB.getMinimumReservationPrice());
            assertThat(couponA.getCouponUseConditionDays()).isEqualTo(couponB.getCouponUseConditionDays());
            assertThat(couponA.getExposureStartDate()).isEqualTo(couponB.getExposureStartDate());
            assertThat(couponA.getExposureEndDate()).isEqualTo(couponB.getExposureEndDate());
        }

        @DisplayName("discountType - 올바른 요청으로 쿠폰을 수정할 수 있다.")
        @Test
        void updateCoupon_discountType_update_success() {

            //when
            executeUpdateCoupon(new CouponUpdateRequest(accommodation.getId(),
                    null,
                    couponB.getDiscountType().getValue(),
                    couponB.getDiscountValue(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ));

            //then
            assertThat(couponA.getDiscountType()).isEqualTo(couponB.getDiscountType());
            assertThat(couponA.getDiscountValue()).isEqualTo(couponB.getDiscountValue());
        }

        private void executeUpdateCoupon(CouponUpdateRequest request) {
            //given
            given(couponRepository.existsByMemberIdAndCouponNumber(anyLong(), anyString())).willReturn(true);
            given(couponRepository.findByCouponNumber(anyString())).willReturn(Optional.of(couponA));
            if (request.registerRooms() != null) {
                given(roomRepository.findByRoomNumber(anyInt())).willReturn(Optional.of(rooms.get(0)));
            }

            //when
            couponService.updateCoupon(member.getId(), couponA.getCouponNumber(), request);
        }
    }

    @DisplayName("쿠폰 노출 여부 변경")
    @Nested
    class CouponExposeTest {
        private Coupon coupon;

        @DisplayName("올바른 요청으로 쿠폰 노출 여부 변경을 할 수 있다.")
        @Test
        void _success() {
            //given
            generateCouponOfBetweenExposureDate();

            CouponStatusType expectedCouponState = CouponStatusType.EXPOSURE_OFF;
            CouponExposeRequest request = new CouponExposeRequest(expectedCouponState.getValue());
            given(couponRepository.existsByMemberIdAndCouponNumber(anyLong(), anyString())).willReturn(true);
            given(couponRepository.findByCouponNumber(anyString())).willReturn(Optional.of(coupon));

            //when
            couponService.exposeCoupon(member.getId(), coupon.getCouponNumber(), request);

            //then
            assertThat(coupon.getCouponStatus()).isEqualTo(expectedCouponState);
        }

        @DisplayName("노출 시작 이후로 대기중을 요청할 경우 쿠폰 노출 여부 변경을 할 수 없다.")
        @Test
        void after_exposure_start_date_but_request_exposure_wait_fail() {
            //given
            generateCouponOfBetweenExposureDate();

            CouponStatusType expectedCouponState = CouponStatusType.EXPOSURE_WAIT;
            CouponExposeRequest request = new CouponExposeRequest(expectedCouponState.getValue());
            given(couponRepository.existsByMemberIdAndCouponNumber(anyLong(), anyString())).willReturn(true);
            given(couponRepository.findByCouponNumber(anyString())).willReturn(Optional.of(coupon));

            //when
            //then
            assertThatThrownBy(() -> couponService.exposeCoupon(member.getId(), coupon.getCouponNumber(), request))
                    .isInstanceOf(InvalidCouponStateWaitExposureDateException.class);
        }

        @DisplayName("노출 기간이 아닌데 ON/OFF를 요청한 경우 쿠폰 노출 여부 변경을 할 수 없다.")
        @ParameterizedTest
        @EnumSource(value = CouponStatusType.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"EXPOSURE_ON", "EXPOSURE_OFF"})
        void not_between_exposure_date_but_request_exposure_OnOff_fail(CouponStatusType expectedCouponState) {
            //given
            generateCouponOfBeforeExposureDate();

            CouponExposeRequest request = new CouponExposeRequest(expectedCouponState.getValue());
            given(couponRepository.existsByMemberIdAndCouponNumber(anyLong(), anyString())).willReturn(true);
            given(couponRepository.findByCouponNumber(anyString())).willReturn(Optional.of(coupon));

            //when
            //then
            assertThatThrownBy(() -> couponService.exposeCoupon(member.getId(), coupon.getCouponNumber(), request))
                    .isInstanceOf(InvalidCouponStateOutsideExposureDateException.class);
        }

        @DisplayName("노출 종료 이전에 종료를 요청한 경우 쿠폰 노출 여부 변경을 할 수 없다.")
        @Test
        void before_exposure_end_date_but_request_exposure_end_fail() {
            //given
            generateCouponOfBetweenExposureDate();

            CouponStatusType expectedCouponState = CouponStatusType.EXPOSURE_END;
            CouponExposeRequest request = new CouponExposeRequest(expectedCouponState.getValue());
            given(couponRepository.existsByMemberIdAndCouponNumber(anyLong(), anyString())).willReturn(true);
            given(couponRepository.findByCouponNumber(anyString())).willReturn(Optional.of(coupon));

            //when
            //then
            assertThatThrownBy(() -> couponService.exposeCoupon(member.getId(), coupon.getCouponNumber(), request))
                    .isInstanceOf(InvalidCouponStateEndExposureDateException.class);
        }

        void generateCouponOfBeforeExposureDate() {
            LocalDate exposureStartDate = LocalDate.now().minusDays(7);
            LocalDate exposureEndDate = exposureStartDate.plusDays(1);
            generateCoupon(exposureStartDate, exposureEndDate);
        }

        void generateCouponOfBetweenExposureDate() {
            LocalDate exposureStartDate = LocalDate.now().minusDays(1);
            LocalDate exposureEndDate = exposureStartDate.plusDays(7);
            generateCoupon(exposureStartDate, exposureEndDate);
        }

        private void generateCoupon(LocalDate startDate, LocalDate endDate) {
            coupon = new CouponTestBuilder(accommodation, member, Collections.emptyList())
                    .exposureDates(startDate, endDate)
                    .build();
            ReflectionTestUtils.setField(coupon, "id", 4321L);
            coupon.generateCouponNumber(CouponIssuerType.OWNER, coupon.getId());
        }
    }

    @DisplayName("쿠폰 삭제")
    @Nested
    class CouponDeleteTest {
        private Coupon coupon;

        @BeforeEach
        void beforeEach() {
            coupon = new CouponTestBuilder(accommodation, member, Collections.emptyList()).build();
            ReflectionTestUtils.setField(coupon, "id", 4321L);
            coupon.generateCouponNumber(CouponIssuerType.OWNER, coupon.getId());
        }

        @DisplayName("올바른 요청으로 쿠폰을 삭제할 수 있다.")
        @Test
        void _success() {
            //given
            given(couponRepository.existsByMemberIdAndCouponNumber(anyLong(), anyString())).willReturn(true);
            given(couponRepository.findByCouponNumber(anyString())).willReturn(Optional.of(coupon));

            //when
            couponService.deleteCoupon(member.getId(), coupon.getCouponNumber());

            //then
            assertThat(coupon.getCouponStatus()).isEqualTo(CouponStatusType.DELETED);
        }

        @DisplayName("쿠폰에 해당하는 멤버 ID가 다르면 쿠폰을 삭제할 수 없다.")
        @Test
        void diff_coupon_and_member_fail() {
            //given
            given(couponRepository.existsByMemberIdAndCouponNumber(anyLong(), anyString())).willReturn(false);

            //when
            //then
            assertThatThrownBy(() -> couponService.deleteCoupon(member.getId(), coupon.getCouponNumber()))
                    .isInstanceOf(CouponAccessDeniedException.class);
        }

        @DisplayName("해당하는 쿠폰 번호가 없으면 쿠폰을 삭제할 수 없다.")
        @Test
        void coupon_not_found_fail() {
            //given
            given(couponRepository.existsByMemberIdAndCouponNumber(anyLong(), anyString())).willReturn(true);
            given(couponRepository.findByCouponNumber(anyString())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> couponService.deleteCoupon(member.getId(), coupon.getCouponNumber()))
                    .isInstanceOf(CouponNotFoundException.class);
        }
    }
}