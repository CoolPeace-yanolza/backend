package com.coolpeace.api.domain.settlement.controller;

import com.coolpeace.api.domain.settlement.dto.request.SearchSettlementParams;
import com.coolpeace.api.domain.settlement.service.SettlementService;
import com.coolpeace.api.global.jwt.security.JwtPrincipal;
import com.coolpeace.api.global.resolver.AuthJwtPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("/{accommodation_id}/summary")
    public ResponseEntity<?> sumSettlement(@PathVariable("accommodation_id") Long accommodationId,
                                           @AuthJwtPrincipal JwtPrincipal jwtPrincipal) {
        return ResponseEntity.ok().body(settlementService.sumSettlement
                (jwtPrincipal.getMemberId(), accommodationId));
    }

    @GetMapping("/{accommodation_id}")
    public ResponseEntity<?> searchSettlement(
            @PathVariable("accommodation_id") Long accommodationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            SearchSettlementParams searchSettlementParams,
            @AuthJwtPrincipal JwtPrincipal jwtPrincipal) {
        return ResponseEntity.ok().body(settlementService.searchSettlement
                (jwtPrincipal.getMemberId(), accommodationId, searchSettlementParams, page, pageSize));
    }

}
