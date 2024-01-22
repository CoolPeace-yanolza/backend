package com.coolpeace.domain.settlement.controller;

import com.coolpeace.domain.settlement.dto.request.SearchSettlementParams;
import com.coolpeace.domain.settlement.service.SettlementService;
import com.coolpeace.global.security.MemberPrincipal;
import com.coolpeace.global.resolver.AuthMemberPrincipal;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
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

    @GetMapping("/{accommodation_id}/summary")
    public ResponseEntity<?> sumSettlement(@PathVariable("accommodation_id") Long accommodationId,
        @AuthMemberPrincipal MemberPrincipal memberPrincipal) {
        return ResponseEntity.ok().body(settlementService.sumSettlement
            (memberPrincipal.getMemberId(), accommodationId));
    }

    @GetMapping("/{accommodation_id}")
    public ResponseEntity<?> searchSettlement(
        @PathVariable("accommodation_id") Long accommodationId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int pageSize,
        @Valid SearchSettlementParams searchSettlementParams,
        @AuthMemberPrincipal MemberPrincipal memberPrincipal) {
        return ResponseEntity.ok().body(settlementService.searchSettlement
            (memberPrincipal.getMemberId(), accommodationId, searchSettlementParams,page,pageSize));
    }

}
