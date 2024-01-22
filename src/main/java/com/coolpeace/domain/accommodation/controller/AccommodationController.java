package com.coolpeace.domain.accommodation.controller;

import com.coolpeace.domain.accommodation.dto.response.AccommodationResponse;
import com.coolpeace.domain.accommodation.dto.response.RoomResponse;
import com.coolpeace.domain.accommodation.service.AccomodationService;
import com.coolpeace.global.jwt.security.MemberPrincipal;
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
    public ResponseEntity<List<AccommodationResponse>> accommodations(
        @AuthJwtPrincipal MemberPrincipal memberPrincipal
    ) {
        return ResponseEntity.ok(accomodationService.getAccommodations(memberPrincipal));
    }

    @GetMapping("/{accommodationId}")
    public ResponseEntity<List<RoomResponse>> rooms(
        @AuthJwtPrincipal MemberPrincipal memberPrincipal,
        @PathVariable Long accommodationId
    ) {
        return ResponseEntity.ok(accomodationService.getRooms(memberPrincipal, accommodationId));
    }

}
