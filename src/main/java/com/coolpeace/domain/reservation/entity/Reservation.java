package com.coolpeace.domain.reservation.entity;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.room.entity.RoomReservation;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.List;
import jdk.dynalink.linker.LinkerServices;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
