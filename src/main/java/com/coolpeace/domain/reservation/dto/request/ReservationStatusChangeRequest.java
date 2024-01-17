package com.coolpeace.domain.reservation.dto.request;

import com.coolpeace.domain.reservation.entity.type.ReservationStatusType;

public record ReservationStatusChangeRequest(
        ReservationStatusType status
) {
}
