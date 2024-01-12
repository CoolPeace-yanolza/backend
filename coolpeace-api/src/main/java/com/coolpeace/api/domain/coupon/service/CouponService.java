package com.coolpeace.api.domain.coupon.service;

import com.coolpeace.api.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.api.domain.coupon.dto.request.CouponExposeRequest;
import com.coolpeace.api.domain.coupon.dto.request.CouponRegisterRequest;
import com.coolpeace.api.domain.coupon.dto.request.CouponUpdateRequest;
import com.coolpeace.api.domain.coupon.dto.response.CouponResponse;
import com.coolpeace.api.domain.coupon.exception.CouponAccessDeniedException;
import com.coolpeace.api.domain.coupon.exception.InvalidCouponStateEndExposureDateException;
import com.coolpeace.api.domain.coupon.exception.InvalidCouponStateOutsideExposureDateException;
import com.coolpeace.api.domain.coupon.exception.InvalidCouponStateWaitExposureDateException;
import com.coolpeace.api.domain.member.exception.MemberNotFoundException;
import com.coolpeace.api.domain.room.exception.RegisterRoomsEmptyException;
import com.coolpeace.api.domain.room.exception.RoomNotFoundException;
import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.core.domain.coupon.dto.request.SearchCouponParams;
import com.coolpeace.core.domain.coupon.entity.Coupon;
import com.coolpeace.core.domain.coupon.entity.type.CouponIssuerType;
import com.coolpeace.core.domain.coupon.entity.type.CouponStatusType;
import com.coolpeace.core.domain.coupon.exception.CouponNotFoundException;
import com.coolpeace.core.domain.coupon.repository.CouponRepository;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.member.repository.MemberRepository;
import com.coolpeace.core.domain.room.entity.Room;
import com.coolpeace.core.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
                couponRegisterRequest.discountType(),
                couponRegisterRequest.discountValue(),
                couponRegisterRequest.customerType(),
                couponRegisterRequest.couponRoomType(),
                couponRegisterRequest.minimumReservationPrice(),
                couponRegisterRequest.couponUseConditionDays(),
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
        storedCoupon.updateCoupon(
                couponUpdateRequest.discountType(),
                couponUpdateRequest.discountValue(),
                couponUpdateRequest.customerType(),
                couponUpdateRequest.couponRoomType(),
                couponUpdateRequest.minimumReservationPrice(),
                couponUpdateRequest.couponUseConditionDays(),
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
        boolean isWaitRequest = couponExposeRequest.couponStatus().equals(CouponStatusType.EXPOSURE_WAIT);
        if (isAfterExposureStartDate && isWaitRequest) {
            throw new InvalidCouponStateWaitExposureDateException();
        }

        // 노출 기간이 아닌데 ON/OFF를 요청한 경우 예외처리
        boolean isBetweenExposureDate = storedCoupon.betweenExposureDate(LocalDate.now());
        boolean isONOFFRequest = couponExposeRequest.couponStatus().equals(CouponStatusType.EXPOSURE_ON)
                || couponExposeRequest.couponStatus().equals(CouponStatusType.EXPOSURE_OFF);
        if (!isBetweenExposureDate && isONOFFRequest) {
            throw new InvalidCouponStateOutsideExposureDateException();
        }

        // 노출 종료 이전에 종료를 요청한 경우 예외처리
        boolean isBeforeExposureEndDate = !(LocalDate.now().isAfter(storedCoupon.getExposureEndDate()));
        boolean isEndRequest = couponExposeRequest.couponStatus().equals(CouponStatusType.EXPOSURE_END);
        if (isBeforeExposureEndDate && isEndRequest) {
            throw new InvalidCouponStateEndExposureDateException();
        }

        storedCoupon.changeCouponStatus(couponExposeRequest.couponStatus());
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
