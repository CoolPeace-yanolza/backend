package com.coolpeace.domain.data.controller;

import com.coolpeace.domain.data.dto.request.GenerateAccommodationRequest;
import com.coolpeace.domain.data.dto.request.GenerateCouponRequest;
import com.coolpeace.domain.data.service.GenerateDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/data")
public class GenerateDataController {


    private final GenerateDataService generateDataService;

    @PostMapping("/accommodation")
    public ResponseEntity<Integer> generateAccommodation(
        @RequestBody GenerateAccommodationRequest req
    ){
        return ResponseEntity.ok(generateDataService.generateAccommodation(req));

    }


    @PostMapping("/coupon")
    public ResponseEntity<Integer> generateCoupon(
        @RequestBody GenerateCouponRequest req
    ){
        return ResponseEntity.ok(generateDataService.generateCoupon(req));
    }


}
