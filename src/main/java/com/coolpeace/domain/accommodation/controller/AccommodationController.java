package com.coolpeace.domain.accommodation.controller;

import com.coolpeace.domain.accommodation.dto.response.AccomodationResponse;
import com.coolpeace.domain.accommodation.dto.response.RoomResponse;
import com.coolpeace.domain.accommodation.service.AccomodationService;
import com.coolpeace.global.jwt.security.JwtPrincipal;
import com.coolpeace.global.resolver.AuthJwtPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accommodation")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccomodationService accomodationService;

    @GetMapping
    public ResponseEntity<List<AccomodationResponse>> accommodations(
        @AuthJwtPrincipal JwtPrincipal jwtPrincipal
    ) {
        return ResponseEntity.ok(accomodationService.getAccommodations(jwtPrincipal));
    }

    @GetMapping("/{accommodationId}")
    public ResponseEntity<List<RoomResponse>> rooms(
        @AuthJwtPrincipal JwtPrincipal jwtPrincipal,
        @PathVariable Long accommodationId
    ) {
        return ResponseEntity.ok(accomodationService.getRooms(jwtPrincipal, accommodationId));
    }

}
