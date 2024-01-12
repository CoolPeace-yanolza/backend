package com.coolpeace.api.domain.coupon.service;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.coupon.dto.response.CouponDailyResponse;
import com.coolpeace.core.domain.coupon.entity.Coupon;
import com.coolpeace.core.domain.coupon.entity.CouponDailyCondition;
import com.coolpeace.core.domain.coupon.repository.CouponRepository;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.room.entity.Room;
import com.coolpeace.api.global.builder.CouponTestBuilder;
import com.coolpeace.api.global.builder.MemberTestBuilder;
import com.coolpeace.api.global.builder.RoomTestBuilder;
import com.coolpeace.api.global.util.RoomTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CouponQueryServiceTest {

    @InjectMocks
    private CouponQueryService couponQueryService;

    @Mock
    private CouponRepository couponRepository;

    @Nested
    @DisplayName("dailyReport()는")
    class dailyReport{
        @Test
        @DisplayName("등록된 쿠폰이 없을 때, 등록된 쿠폰이 없음을 내보낸다.")
        void no_register(){
            //given
            given(couponRepository.noRegister(anyLong(), anyLong()))
                .willReturn(Boolean.TRUE);
            //when
            CouponDailyResponse couponDailyResponse =
                couponQueryService.dailyReport("1", 1L);
            //then
            assertThat(couponDailyResponse.condition()).isEqualTo(CouponDailyCondition.NO_REGISTER);
        }
        @Test
        @DisplayName("노출기간이 3일 이내로 남은 쿠폰이 있으면, 조건에 맞는 쿠폰 이름을 내보낸다.")
        void expiration3days(){
            //given
            Member member = new MemberTestBuilder().encoded().build();
            Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
            List<Room> rooms = new RoomTestBuilder(accommodation).buildList();
            List<Room> randomRooms = RoomTestUtil.getRandomRooms(rooms);
            Coupon coupon1 = new CouponTestBuilder(accommodation, member, randomRooms).build();
            Coupon coupon2 = new CouponTestBuilder(accommodation, member, randomRooms).build();
            List<Coupon> couponList = new ArrayList<>();
            couponList.add(coupon1);
            couponList.add(coupon2);

            given(couponRepository.noRegister(anyLong(), anyLong()))
                .willReturn(Boolean.FALSE);
            given(couponRepository.expiration3days(anyLong(), anyLong()))
                .willReturn(couponList);
            //when
            CouponDailyResponse couponDailyResponse =
                couponQueryService.dailyReport("1", 1L);
            //then
            assertThat(couponDailyResponse.condition()).isEqualTo(CouponDailyCondition.EXPIRATION_3DAYS);
            assertThat(couponDailyResponse.couponTitles().get(0)).isEqualTo(coupon1.getCouponTitle());
            assertThat(couponDailyResponse.couponTitles().get(1)).isEqualTo(coupon2.getCouponTitle());
        }
        @Test
        @DisplayName("노출 중인 쿠폰이 없으면, 노출 중인 쿠폰이 없음을 내보낸다.")
        void no_exposure(){
            //given

            given(couponRepository.noRegister(anyLong(), anyLong()))
                .willReturn(Boolean.FALSE);
            given(couponRepository.expiration3days(anyLong(), anyLong()))
                .willReturn(Collections.emptyList());
            given(couponRepository.noExposure(anyLong(), anyLong()))
                .willReturn(Boolean.TRUE);
            //when
            CouponDailyResponse couponDailyResponse =
                couponQueryService.dailyReport("1", 1L);
            //then
            assertThat(couponDailyResponse.condition()).isEqualTo(CouponDailyCondition.NO_EXPOSURE);

        }
        @Test
        @DisplayName("아무 조건에 해당하지 않으면, 노출중인 쿠폰들을 내보낸다.")
        void no_condition(){
            //given
            Member member = new MemberTestBuilder().encoded().build();
            Accommodation accommodation = new Accommodation(1L, "신라호텔", "주소주소", member);
            List<Room> rooms = new RoomTestBuilder(accommodation).buildList();
            List<Room> randomRooms = RoomTestUtil.getRandomRooms(rooms);
            Coupon coupon1 = new CouponTestBuilder(accommodation, member, randomRooms).build();
            Coupon coupon2 = new CouponTestBuilder(accommodation, member, randomRooms).build();
            List<Coupon> couponList = new ArrayList<>();
            couponList.add(coupon1);
            couponList.add(coupon2);

            given(couponRepository.noRegister(anyLong(), anyLong()))
                .willReturn(Boolean.FALSE);
            given(couponRepository.expiration3days(anyLong(), anyLong()))
                .willReturn(Collections.emptyList());
            given(couponRepository.noExposure(anyLong(), anyLong()))
                .willReturn(Boolean.FALSE);
            given(couponRepository.exposureCoupons(anyLong(), anyLong()))
                .willReturn(couponList);
            //when
            CouponDailyResponse couponDailyResponse =
                couponQueryService.dailyReport("1", 1L);
            //then
            assertThat(couponDailyResponse.condition()).isEqualTo(CouponDailyCondition.NO_CONDITION);
            assertThat(couponDailyResponse.couponTitles().get(0)).isEqualTo(coupon1.getCouponTitle());
            assertThat(couponDailyResponse.couponTitles().get(1)).isEqualTo(coupon2.getCouponTitle());
        }
    }



}
