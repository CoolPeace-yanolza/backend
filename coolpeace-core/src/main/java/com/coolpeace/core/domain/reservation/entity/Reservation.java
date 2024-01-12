package com.coolpeace.core.domain.reservation.entity;

import com.coolpeace.core.common.BaseTimeEntity;
import com.coolpeace.core.domain.room.entity.RoomReservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Column(nullable = false)
    private int totalPrice = 0;

    @Column(nullable = false)
    private int discountPrice = 0;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<RoomReservation> roomReservations;

}
