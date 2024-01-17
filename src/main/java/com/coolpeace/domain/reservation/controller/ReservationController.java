package com.coolpeace.domain.reservation.controller;

import com.coolpeace.domain.reservation.dto.request.ReservationCreateRequest;
import com.coolpeace.domain.reservation.dto.request.ReservationStatusChangeRequest;
import com.coolpeace.domain.reservation.dto.response.ReservationPageResponse;
import com.coolpeace.domain.reservation.dto.response.ReservationResponse;
import com.coolpeace.domain.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ReservationPageResponse> getAllReservations(
            @PageableDefault Pageable pageable
    ) {
        Page<ReservationResponse> allReservations = reservationService.getAllReservations(pageable);
        return ResponseEntity.ok()
                .body(ReservationPageResponse.from(allReservations));
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @Valid @RequestBody ReservationCreateRequest createRequest
    ) {
        reservationService.createReservation(createRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @PutMapping("/{reservation_id}")
    public ResponseEntity<Void> changeReservationStatus(
            @PathVariable("reservation_id") Long reservationId,
            @Valid @RequestBody ReservationStatusChangeRequest changeRequest
    ) {
        reservationService.changeReservationStatus(reservationId, changeRequest);
        return ResponseEntity.ok().build();
    }
}
