package com.coolpeace.domain.coupon.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.dto.request.type.SearchCouponDateFilterType;
import com.coolpeace.domain.coupon.dto.request.type.SearchCouponStatusFilterType;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.CouponIssuerType;
import com.coolpeace.domain.coupon.entity.type.CouponStatusType;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.room.repository.RoomRepository;
import com.coolpeace.global.builder.AccommodationTestBuilder;
import com.coolpeace.global.builder.MemberTestBuilder;
import com.coolpeace.global.builder.RoomTestBuilder;
import com.coolpeace.global.common.QueryDSLRepositoryTest;
import com.coolpeace.global.util.CouponTestUtil;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CouponRepositoryTest extends QueryDSLRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CouponRepository couponRepository;

    private final Faker faker = new Faker();

    private Member member;
    private Accommodation accommodation;
    private List<Room> rooms;
    private List<Coupon> coupons;

    @BeforeEach
    void beforeEach() {

        // 로그인 (영속화)
        this.member = memberRepository.save(new MemberTestBuilder().encoded().build());

        // 숙박 및 객실 저장 (영속화)
        this.accommodation = accommodationRepository.save(new AccommodationTestBuilder(this.member).build());
        this.rooms = roomRepository.saveAll(new RoomTestBuilder(this.accommodation).buildList());

        // 쿠폰 리스트 (영속화)
        this.coupons = couponRepository.saveAll(
                CouponTestUtil.getTestCoupons(this.accommodation, this.member, this.rooms));
        this.coupons.forEach(coupon -> coupon.generateCouponNumber(CouponIssuerType.OWNER, coupon.getId()));
    }

    @DisplayName("쿠폰 검색 파라미터로 쿠폰을 검색할 수 있다.")
    @Test
    void findAllCoupons_success() {
        //given
        SearchCouponParams params = new SearchCouponParams(
                SearchCouponStatusFilterType.EXPOSURE_OFF,
                null,
                SearchCouponDateFilterType.SIX_MONTHS
        );

        //when
        Page<Coupon> couponPage = couponRepository.findAllCoupons(member.getId(), params, PageRequest.of(0, 10));

        //then
        assertThat(couponPage).isNotNull();
        assertThat(couponPage.isFirst()).isEqualTo(true);
        if (!couponPage.isEmpty()) {
            Optional<Coupon> couponOptional = couponPage.get().findFirst();
            if (couponOptional.isEmpty()) {
                Assertions.fail();
            }
            Coupon coupon = couponOptional.get();
            assertThat(coupon.getCouponStatus() == CouponStatusType.EXPOSURE_OFF
                || coupon.getCouponStatus() == CouponStatusType.EXPOSURE_WAIT).isTrue();
            assertThat(coupon.getCreatedAt()).isAfter(LocalDateTime.now().minusMonths(6));
        }
    }

    @DisplayName("쿠폰의 이전 쿠폰 등록 내역 조회")
    @Test
    void findRecentCouponByMemberId_success() {
        //given
        long memberId = member.getId();

        //when
        List<Coupon> resultCoupons = couponRepository.findRecentCouponByMemberId(memberId);

        //then
        assertThat(resultCoupons.size()).isLessThanOrEqualTo(4);
        Coupon coupon = resultCoupons.get(0);
        assertThat(coupon.getMember().getId()).isEqualTo(memberId);
    }

    @DisplayName("쿠폰의 번호로 쿠폰을 조회할 수 있다.")
    @Test
    void findByCouponNumber_success() {
        //given
        Coupon pickCoupon = coupons.get(faker.number().numberBetween(0, coupons.size()));

        //when
        Optional<Coupon> result = couponRepository.findByCouponNumber(pickCoupon.getCouponNumber());

        //then
        assertThat(result.isPresent()).isTrue();
        Coupon coupon = result.get();
        assertThat(coupon.getCouponNumber()).isEqualTo(pickCoupon.getCouponNumber());
        assertThat(coupon.getCouponTitle()).isEqualTo(pickCoupon.getCouponTitle());
    }
}