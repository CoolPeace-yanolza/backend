package com.coolpeace.api.domain.accommodation.controller;

import com.coolpeace.api.domain.accommodation.dto.response.AccomodationResponse;
import com.coolpeace.api.global.jwt.security.JwtPrincipal;
import com.coolpeace.api.domain.accommodation.dto.response.RoomResponse;
import com.coolpeace.api.domain.accommodation.service.AccomodationService;
import com.coolpeace.api.global.resolver.AuthJwtPrincipal;
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
    public ResponseEntity<List<AccommodationResponse>> accommodations(
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
