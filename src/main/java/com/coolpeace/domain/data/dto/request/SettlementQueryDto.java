package com.coolpeace.domain.data.dto.request;

import com.coolpeace.domain.reservation.entity.type.ReservationStatusType;
import java.time.LocalDateTime;

public record SettlementQueryDto (
    Long id,

    LocalDateTime startDate,

    LocalDateTime endDate,

    Integer totalPrice,

    Integer discountPrice,

    ReservationStatusType reservationStatus,

    Long couponId

)
{


}
