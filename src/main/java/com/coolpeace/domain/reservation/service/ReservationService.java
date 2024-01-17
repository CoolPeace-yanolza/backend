package com.coolpeace.domain.reservation.service;

import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.exception.CouponNotFoundException;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import com.coolpeace.domain.reservation.dto.request.ReservationCreateRequest;
import com.coolpeace.domain.reservation.dto.request.ReservationStatusChangeRequest;
import com.coolpeace.domain.reservation.dto.response.ReservationResponse;
import com.coolpeace.domain.reservation.entity.Reservation;
import com.coolpeace.domain.reservation.exception.ReservationNotFoundException;
import com.coolpeace.domain.reservation.repository.ReservationRepository;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.room.entity.RoomReservation;
import com.coolpeace.domain.room.exception.RoomNotFoundException;
import com.coolpeace.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final AccommodationRepository accommodationRepository;
    private final CouponRepository couponRepository;
    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public Page<ReservationResponse> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable).map(ReservationResponse::from);
    }

    @Transactional
    public void createReservation(ReservationCreateRequest request) {
        Reservation newReservation = Reservation.from(request.startDate(), request.endDate());
        if (!accommodationRepository.existsById(request.accommodationId())) {
            throw new AccommodationNotFoundException();
        }

        List<RoomReservation> roomReservations = request.rooms().stream().map(roomWithCoupon -> {
            String roomNumberStr = roomWithCoupon.roomNumber();
            int roomNumber = getRoomNumberFromStr(roomNumberStr);
            Room room = roomRepository.findByRoomNumber(roomNumber).orElseThrow(RoomNotFoundException::new);
            Coupon coupon = couponRepository.findByCouponNumber(roomWithCoupon.couponNumber())
                    .orElseThrow(CouponNotFoundException::new);
            return RoomReservation.from(room, newReservation, coupon);
        }).toList();
        int totalPrice = roomReservations.stream()
                .mapToInt(roomRes -> roomRes.getRoom().getPrice()).sum();

        newReservation.updateRoomReservationAndPrices(roomReservations, totalPrice);
        reservationRepository.save(newReservation);
    }

    @Transactional
    public void changeReservationStatus(Long reservationId, ReservationStatusChangeRequest changeRequest) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);
        reservation.updateReservation(changeRequest.status());
    }

    private int getRoomNumberFromStr(String roomNumberStr) {
        int roomNumber;
        if (roomNumberStr.endsWith("호")) {
            roomNumber = Integer.parseInt(roomNumberStr.substring(0, roomNumberStr.length() - 1));
        } else {
            roomNumber = Integer.parseInt(roomNumberStr);
        }
        return roomNumber;
    }
}