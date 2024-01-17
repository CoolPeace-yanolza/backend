package com.coolpeace.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationCreateRequest(
        @NotNull(message = "예약 시작 날짜를 입력해야 합니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startDate,
        @NotNull(message = "예약 종료 날짜를 입력해야 합니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endDate,
        @NotNull(message = "숙박업소의 ID를 입력해야 합니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        Long accommodationId,
        @NotEmpty(message = "예약할 방 번호 리스트를 입력해야 합니다.")
        List<ReservationRoomNumberWithCoupon> rooms
) {
        public record ReservationRoomNumberWithCoupon (
                String roomNumber,
                String couponNumber
        ) {
        }
}
