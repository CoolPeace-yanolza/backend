package com.coolpeace.domain.coupon.service;

import com.coolpeace.docs.utils.AccommodationTestUtil;
import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.dto.request.type.SearchCouponDateFilterType;
import com.coolpeace.domain.coupon.dto.request.type.SearchCouponStatusFilterType;
import com.coolpeace.domain.coupon.dto.response.CouponResponse;
import com.coolpeace.domain.coupon.entity.Coupon;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

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
            coupons = getTestCoupons();
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
                        params.add(new SearchCouponParams(statusType, title, dateType));
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
            SearchCouponParams searchCouponParams = new SearchCouponParams(null, null, SearchCouponDateFilterType.YEAR);
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
            coupons = getTestCoupons();
        }

        @DisplayName("이전 쿠폰 등록 내역을 조회할 수 있다.")
        @Test
        void no_filter_success() {
            // given
            given(couponRepository.findRecentCouponByMemberId(anyLong())).willReturn(
                    coupons.stream().limit(4).toList());

            // when
            List<CouponResponse> result = couponService.getRecentHistory(member.getId());

            // then
            verify(couponRepository).findRecentCouponByMemberId(anyLong());

            assertThat(result).isNotNull();
        }
    }

    @DisplayName("쿠폰 등록")
    @Nested
    class CouponRegisterTest {
        private List<Integer> registerRoomNumbers;
        private Coupon coupon;

        @BeforeEach
        void beforeEach() {
            List<Room> registerRooms = AccommodationTestUtil.getRandomRooms(rooms);
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
                    coupon.getCustomerType(),
                    coupon.getDiscountType(),
                    coupon.getDiscountValue(),
                    coupon.getCouponRoomType(),
                    accommodation.getId(),
                    registerAllRoom,
                    !registerAllRoom ? registerRoomNumbers : null,
                    coupon.getMinimumReservationPrice(),
                    coupon.getCouponUseConditionDays(),
                    coupon.getExposureStartDate(),
                    coupon.getExposureEndDate()
            );
        }
    }

    public List<Coupon> getTestCoupons() {
        return IntStream.range(0, 20)
                .mapToObj(i -> {
                    List<Room> registerRooms = AccommodationTestUtil.getRandomRooms(rooms);
                    Coupon coupon = new CouponTestBuilder(accommodation, member, registerRooms).build();
                    ReflectionTestUtils.setField(coupon, "id", 4321L);
                    ReflectionTestUtils.setField(coupon, "createdAt", LocalDateTime.now());
                    return coupon;
                })
                .toList();
    }
}