package com.coolpeace.domain.coupon.service;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.dto.request.CouponExposeRequest;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.dto.response.CouponResponse;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.room.exception.RoomNotFoundException;
import com.coolpeace.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public Coupon getRecentHistory() {
        return null;
    }

    public void register(Long memberId, CouponRegisterRequest couponRegisterRequest) {
        Member storedMember = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Accommodation accommodation = accommodationRepository.findById(couponRegisterRequest.accommodationId())
                .orElseThrow(AccommodationNotFoundException::new);

        if (couponRegisterRequest.registerAllRoom()) {
            couponRepository.save(Coupon.from(
                    couponRegisterRequest.title(),
                    couponRegisterRequest.discountType(),
                    couponRegisterRequest.discountValue(),
                    couponRegisterRequest.customerType(),
                    couponRegisterRequest.couponRoomType(),
                    couponRegisterRequest.minimumReservationPrice(),
                    couponRegisterRequest.couponUseConditionDays(),
                    couponRegisterRequest.exposureStartDate(),
                    couponRegisterRequest.exposureEndDate(),
                    accommodation,
                    storedMember
            ));
        } else {
            List<Room> rooms = couponRegisterRequest.registerRooms()
                    .stream().map(roomNumber -> roomRepository.findByRoomNumber(roomNumber)
                            .orElseThrow(RoomNotFoundException::new))
                    .toList();
            rooms.forEach(room -> couponRepository.save(Coupon.from(
                    couponRegisterRequest.title(),
                    couponRegisterRequest.discountType(),
                    couponRegisterRequest.discountValue(),
                    couponRegisterRequest.customerType(),
                    couponRegisterRequest.couponRoomType(),
                    couponRegisterRequest.minimumReservationPrice(),
                    couponRegisterRequest.couponUseConditionDays(),
                    couponRegisterRequest.exposureStartDate(),
                    couponRegisterRequest.exposureEndDate(),
                    room,
                    storedMember
            )));
        }
    }

    public Page<CouponResponse> searchCoupons(Long memberId, SearchCouponParams searchCouponParams, Pageable pageable) {
        return couponRepository.findAllCoupons(memberId, searchCouponParams,
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()))
                .map(CouponResponse::from);
    }

    public void updateCoupon(String couponId, CouponUpdateRequest couponExposeRequest) {
    }

    public void exposeCoupon(String couponId, CouponExposeRequest couponExposeRequest) {
    }

    public void deleteCoupon(String couponId) {
    }
}
