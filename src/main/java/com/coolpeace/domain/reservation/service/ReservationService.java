package com.coolpeace.domain.reservation.service;

import com.coolpeace.domain.reservation.dto.request.ReservationCreateRequest;
import com.coolpeace.domain.reservation.dto.request.ReservationStatusChangeRequest;
import com.coolpeace.domain.reservation.entity.Reservation;
import com.coolpeace.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Page<Reservation> getAllReservations(Pageable pageable) {
        return null;
    }

    public void createReservation(Long memberId, ReservationCreateRequest request) {

    }

    public void changeReservationStatus(Long memberId, Long reservationId, ReservationStatusChangeRequest changeRequest) {

    }
}
