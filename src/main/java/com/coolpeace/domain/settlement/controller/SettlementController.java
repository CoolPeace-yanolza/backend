package com.coolpeace.domain.settlement.controller;

import com.coolpeace.domain.settlement.dto.request.SearchSettlementParams;
import com.coolpeace.domain.settlement.service.SettlementService;
import com.coolpeace.global.jwt.security.JwtPrincipal;
import com.coolpeace.global.resolver.AuthJwtPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("/{accommodation_id}")
    public ResponseEntity<?> sumSettlement(@PathVariable("accommodation_id") Long accommodationId,
        @AuthJwtPrincipal JwtPrincipal jwtPrincipal) {
        return ResponseEntity.ok().body(settlementService.sumSettlement
            (Long.valueOf(jwtPrincipal.getMemberId()), accommodationId));
    }

    @GetMapping("/{accommodation_id}/summary")
    public ResponseEntity<?> searchSettlement(
        @PathVariable("accommodation_id") Long accommodationId,
        @PageableDefault Pageable pageable,
        SearchSettlementParams searchSettlementParams,
        @AuthJwtPrincipal JwtPrincipal jwtPrincipal) {
        return ResponseEntity.ok().body(settlementService.searchSettlement(Long.valueOf
            (jwtPrincipal.getMemberId()), accommodationId, searchSettlementParams,pageable));
    }

}
