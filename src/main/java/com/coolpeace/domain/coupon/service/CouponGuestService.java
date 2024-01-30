package com.coolpeace.domain.coupon.service;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.domain.coupon.dto.request.type.DtoCouponUseDayOfWeekType;
import com.coolpeace.domain.coupon.dto.request.type.DtoCouponUseDaysType;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.type.*;
import com.coolpeace.domain.coupon.exception.CouponAccessDeniedException;
import com.coolpeace.domain.coupon.exception.CouponNotFoundException;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.room.exception.RegisterRoomsEmptyException;
import com.coolpeace.domain.room.exception.RoomNotFoundException;
import com.coolpeace.domain.room.repository.RoomRepository;
import com.coolpeace.global.common.ValuedEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CouponGuestService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public void register(Long memberId, CouponRegisterRequest couponRegisterRequest) {
        Member storedMember = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        Accommodation accommodation = accommodationRepository.findById(
                        couponRegisterRequest.accommodationId())
                .orElseThrow(AccommodationNotFoundException::new);

        List<Room> rooms;
        if (!couponRegisterRequest.registerAllRoom()) {
            if (CollectionUtils.isEmpty(couponRegisterRequest.registerRooms())) {
                throw new RegisterRoomsEmptyException();
            }
            rooms = couponRegisterRequest.registerRooms()
                    .stream().map(roomNumber -> roomRepository.findByRoomNumber(roomNumber)
                            .orElseThrow(RoomNotFoundException::new))
                    .toList();
        } else {
            rooms = Collections.emptyList();
        }

        DiscountType discountType = ValuedEnum.of(DiscountType.class,
                couponRegisterRequest.discountType());
        Integer discountValue = switch (discountType) {
            case FIXED_RATE -> couponRegisterRequest.discountFlatRate();
            case FIXED_PRICE -> couponRegisterRequest.discountFlatValue();
        };

        CouponRoomType couponRoomTypeValue = getCouponRoomType(
                couponRegisterRequest.couponRoomTypes(), CouponRoomType.RENTAL);
        CouponRoomType couponRoomStayTypeValue = getCouponRoomStayTypeValue(
                couponRegisterRequest.couponRoomTypes(), couponRoomTypeValue);

        CouponUseDaysType couponUseDays = getCouponUseDays(
                couponRegisterRequest.couponUseConditionDays(),
                couponRegisterRequest.couponUseConditionDayOfWeek());
    }

    private CouponUseDaysType getCouponUseDays(String couponUseDaysStr, String couponUseDayOfWeekStr) {
        if (!StringUtils.hasText(couponUseDaysStr)) {
            return CouponUseDaysType.ALL;
        }
        DtoCouponUseDaysType dtoCouponUseDaysType = ValuedEnum.of(DtoCouponUseDaysType.class, couponUseDaysStr);
        return switch (dtoCouponUseDaysType) {
            case ONEDAY -> CouponUseDaysType.valueOf(
                    ValuedEnum.of(DtoCouponUseDayOfWeekType.class, couponUseDayOfWeekStr).name());
            case WEEKDAY -> CouponUseDaysType.WEEKDAY;
            case WEEKEND -> CouponUseDaysType.WEEKEND;
        };
    }

    @Transactional
    public void updateCoupon(Long memberId, String couponNumber,
                             CouponUpdateRequest couponUpdateRequest) {
        validateMemberHasCoupon(memberId, couponNumber);
        Coupon storedCoupon = couponRepository.findByCouponNumber(couponNumber)
                .orElseThrow(CouponNotFoundException::new);
        List<Room> rooms;
        if (couponUpdateRequest.registerRooms() != null) {
            rooms = couponUpdateRequest.registerRooms()
                    .stream().map(roomNumber -> roomRepository.findByRoomNumber(roomNumber)
                            .orElseThrow(RoomNotFoundException::new))
                    .toList();
        } else {
            rooms = Collections.emptyList();
        }

        DiscountType discountType = Optional.ofNullable(couponUpdateRequest.discountType())
                .map(str -> ValuedEnum.of(DiscountType.class, str))
                .orElse(null);

        Integer discountValue = null;
        if (discountType != null) {
            discountValue = switch (discountType) {
                case FIXED_RATE -> couponUpdateRequest.discountFlatRate();
                case FIXED_PRICE -> couponUpdateRequest.discountFlatValue();
            };
        }

        CustomerType customerType = Optional.ofNullable(couponUpdateRequest.customerType())
                .map(str -> ValuedEnum.of(CustomerType.class, str))
                .orElse(null);

        CouponRoomType couponRoomTypeValue = getCouponRoomType(
                couponUpdateRequest.couponRoomTypes(), CouponRoomType.RENTAL);
        CouponRoomType couponRoomStayTypeValue = getCouponRoomStayTypeValue(
                couponUpdateRequest.couponRoomTypes(), couponRoomTypeValue);

        CouponUseDaysType couponUseDays = Optional.ofNullable(couponUpdateRequest.couponUseConditionDays())
                .map(str -> getCouponUseDays(
                        couponUpdateRequest.couponUseConditionDays(),
                        couponUpdateRequest.couponUseConditionDayOfWeek()))
                .orElse(null);
    }

    private CouponRoomType getCouponRoomStayTypeValue(List<String> couponRoomTypeStrings,
                                                      CouponRoomType couponRoomTypeRentalValue) {
        if (couponRoomTypeRentalValue != null) {
            return null;
        }
        CouponRoomType couponRoomType = getCouponRoomType(couponRoomTypeStrings, CouponRoomType.LODGE);
        if (couponRoomType == null) {
            return getCouponRoomType(couponRoomTypeStrings, CouponRoomType.TWO_NIGHT);
        }
        return couponRoomType;
    }

    private CouponRoomType getCouponRoomType(List<String> request, CouponRoomType couponRoomType) {
        return request.stream()
                .map(str -> ValuedEnum.of(CouponRoomType.class, str))
                .filter(roomType -> roomType.equals(couponRoomType))
                .findFirst().orElse(null);
    }

    @Transactional
    public void deleteCoupon(Long memberId, String couponNumber) {
        validateMemberHasCoupon(memberId, couponNumber);
        Coupon storedCoupon = couponRepository.findByCouponNumber(couponNumber)
                .orElseThrow(CouponNotFoundException::new);
    }

    public void validateMemberHasCoupon(Long memberId, String couponNumber) {
        if (!couponRepository.existsByMemberIdAndCouponNumber(memberId, couponNumber)) {
            throw new CouponAccessDeniedException();
        }
    }
}
