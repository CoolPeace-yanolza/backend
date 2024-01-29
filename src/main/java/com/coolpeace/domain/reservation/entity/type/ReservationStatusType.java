package com.coolpeace.domain.reservation.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatusType {
    PENDING("결제 예정"),
    CONFIRMED("예약 완료"),
    CANCELLED("예약 취소");

    private final String value;
}
