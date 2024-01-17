package com.coolpeace.domain.reservation.controller;

import com.coolpeace.domain.reservation.dto.request.ReservationCreateRequest;
import com.coolpeace.domain.reservation.dto.request.ReservationStatusChangeRequest;
import com.coolpeace.domain.reservation.dto.response.ReservationResponse;
import com.coolpeace.domain.reservation.service.ReservationService;
import com.coolpeace.global.jwt.security.JwtPrincipal;
import com.coolpeace.global.resolver.AuthJwtPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ReservationResponse> getAllReservations(
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok()
                .body(ReservationResponse.from(reservationService.getAllReservations(pageable)));
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @Valid @RequestBody ReservationCreateRequest createRequest,
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        reservationService.createReservation(Long.valueOf(jwtPrincipal.getMemberId()), createRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @PutMapping("/{reservation_id}")
    public ResponseEntity<Void> changeReservationStatus(
            @PathVariable("reservation_id") Long reservationId,
            @Valid @RequestBody ReservationStatusChangeRequest changeRequest,
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        reservationService.changeReservationStatus(Long.valueOf(jwtPrincipal.getMemberId()), reservationId, changeRequest);
        return ResponseEntity.ok().build();
    }
}
