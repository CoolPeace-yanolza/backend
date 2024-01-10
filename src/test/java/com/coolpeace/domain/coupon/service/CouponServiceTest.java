package com.coolpeace.domain.coupon.service;

import com.coolpeace.docs.utils.AccommodationTestUtil;
import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
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
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @DisplayName("쿠폰 등록")
    @Nested
    class CouponRegisterTest {

        private Member member;
        private Accommodation accommodation;
        private List<Room> rooms;
        private List<Integer> registerRoomNumbers;
        private Coupon coupon;


        @BeforeEach
        void beforeEach() {
            this.member = new MemberTestBuilder().encoded().build();
            this.accommodation = new AccommodationTestBuilder(member).build();
            this.rooms = new RoomTestBuilder(accommodation).buildList();
            List<Room> registerRooms = AccommodationTestUtil.getRandomRooms(rooms);
            this.registerRoomNumbers = registerRooms.stream().map(Room::getRoomNumber).toList();

            coupon = new CouponTestBuilder(accommodation, member, registerRooms).build();

            ReflectionTestUtils.setField(member, "id", 101L);
            ReflectionTestUtils.setField(accommodation, "id", 1234L);
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
}