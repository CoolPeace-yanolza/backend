package com.coolpeace.domain.coupon.service;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.dto.request.CouponExposeRequest;
import com.coolpeace.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.domain.coupon.dto.response.CouponCategoryResponse;
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
import com.coolpeace.global.common.DayOfWeekUtil;
import com.coolpeace.global.common.ValuedEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public Page<CouponResponse> searchCoupons(Long memberId, SearchCouponParams searchCouponParams, Pageable pageable) {
        return couponRepository.findAllCoupons(memberId, searchCouponParams,
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                                pageable.getSortOr(Sort.by(Sort.Direction.DESC, "createdAt"))))
                .map(CouponResponse::from);
    }

    @Transactional(readOnly = true)
    public CouponCategoryResponse getCouponCategories() {
        Map<CouponStatusType, Long> counts = couponRepository.countCouponsByCouponStatus();

        long all = counts.values().stream().mapToLong(Long::longValue).sum();
        long exposureOn = counts.getOrDefault(CouponStatusType.EXPOSURE_ON, 0L);
        long exposureOff = counts.getOrDefault(CouponStatusType.EXPOSURE_OFF, 0L)
                + counts.getOrDefault(CouponStatusType.EXPOSURE_WAIT, 0L);
        long expired = counts.getOrDefault(CouponStatusType.EXPOSURE_END, 0L)
                + counts.getOrDefault(CouponStatusType.DELETED, 0L);

        return CouponCategoryResponse.from(all, exposureOn, exposureOff, expired);
    }

    @Transactional(readOnly = true)
    public List<CouponResponse> getRecentHistory(Long memberId) {
        return couponRepository.findRecentCouponByMemberId(memberId)
                .stream().map(CouponResponse::from).toList();
    }

    @Transactional
    public void register(Long memberId, CouponRegisterRequest couponRegisterRequest) {
        Member storedMember = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Accommodation accommodation = accommodationRepository.findById(couponRegisterRequest.accommodationId())
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

        Coupon savedCoupon = couponRepository.save(Coupon.from(
                couponRegisterRequest.title(),
                ValuedEnum.of(DiscountType.class, couponRegisterRequest.discountType()),
                couponRegisterRequest.discountValue(),
                ValuedEnum.of(CustomerType.class, couponRegisterRequest.customerType()),
                ValuedEnum.of(CouponRoomType.class, couponRegisterRequest.couponRoomType()),
                couponRegisterRequest.minimumReservationPrice(),
                DayOfWeekUtil.fromDayOfWeekStrings(couponRegisterRequest.couponUseConditionDays())
                ,
                couponRegisterRequest.exposureStartDate(),
                couponRegisterRequest.exposureEndDate(),
                accommodation,
                rooms,
                storedMember
        ));
        savedCoupon.generateCouponNumber(CouponIssuerType.OWNER, savedCoupon.getId());
    }

    @Transactional
    public void updateCoupon(Long memberId, String couponNumber, CouponUpdateRequest couponUpdateRequest) {
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
        CustomerType customerType = Optional.ofNullable(couponUpdateRequest.customerType())
                .map(str -> ValuedEnum.of(CustomerType.class, str))
                .orElse(null);
        CouponRoomType couponRoomType = Optional.ofNullable(couponUpdateRequest.couponRoomType())
                .map(str -> ValuedEnum.of(CouponRoomType.class, str))
                .orElse(null);
        List<DayOfWeek> dayOfWeeks = Optional.ofNullable(couponUpdateRequest.couponUseConditionDays())
                .map(DayOfWeekUtil::fromDayOfWeekStrings)
                .orElse(null);

        storedCoupon.updateCoupon(
                discountType,
                couponUpdateRequest.discountValue(),
                customerType,
                couponRoomType,
                couponUpdateRequest.minimumReservationPrice(),
                dayOfWeeks,
                rooms,
                couponUpdateRequest.exposureStartDate(),
                couponUpdateRequest.exposureEndDate()
        );
    }

    @Transactional
    public void exposeCoupon(Long memberId, String couponNumber, CouponExposeRequest couponExposeRequest) {
        validateMemberHasCoupon(memberId, couponNumber);
        Coupon storedCoupon = couponRepository.findByCouponNumber(couponNumber)
                .orElseThrow(CouponNotFoundException::new);

        // 노출 시작 이후로 대기중을 요청한 경우 예외처리
        boolean isAfterExposureStartDate = !(LocalDate.now().isBefore(storedCoupon.getExposureStartDate()));
        boolean isWaitRequest = couponExposeRequest.couponStatus().equals(CouponStatusType.EXPOSURE_WAIT.getValue());
        if (isAfterExposureStartDate && isWaitRequest) {
            throw new InvalidCouponStateWaitExposureDateException();
        }

        // 노출 기간이 아닌데 ON/OFF를 요청한 경우 예외처리
        boolean isBetweenExposureDate = storedCoupon.betweenExposureDate(LocalDate.now());
        boolean isONOFFRequest = couponExposeRequest.couponStatus().equals(CouponStatusType.EXPOSURE_ON.getValue())
                || couponExposeRequest.couponStatus().equals(CouponStatusType.EXPOSURE_OFF.getValue());
        if (!isBetweenExposureDate && isONOFFRequest) {
            throw new InvalidCouponStateOutsideExposureDateException();
        }

        // 노출 종료 이전에 종료를 요청한 경우 예외처리
        boolean isBeforeExposureEndDate = !(LocalDate.now().isAfter(storedCoupon.getExposureEndDate()));
        boolean isEndRequest = couponExposeRequest.couponStatus().equals(CouponStatusType.EXPOSURE_END.getValue());
        if (isBeforeExposureEndDate && isEndRequest) {
            throw new InvalidCouponStateEndExposureDateException();
        }

        storedCoupon.changeCouponStatus(ValuedEnum.of(CouponStatusType.class, couponExposeRequest.couponStatus()));
    }

    @Transactional
    public void deleteCoupon(Long memberId, String couponNumber) {
        validateMemberHasCoupon(memberId, couponNumber);
        Coupon storedCoupon = couponRepository.findByCouponNumber(couponNumber)
                .orElseThrow(CouponNotFoundException::new);
        storedCoupon.changeCouponStatus(CouponStatusType.DELETED);
    }

    public void validateMemberHasCoupon(Long memberId, String couponNumber) {
        if (!couponRepository.existsByMemberIdAndCouponNumber(memberId, couponNumber)) {
            throw new CouponAccessDeniedException();
        }
    }
}
