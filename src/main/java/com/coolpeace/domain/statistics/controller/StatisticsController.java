package com.coolpeace.domain.statistics.controller;

import com.coolpeace.domain.statistics.service.DailyStatisticsService;
import com.coolpeace.domain.statistics.service.MonthlyStatisticsService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/statistics/setting")
@RequiredArgsConstructor
public class StatisticsController {

    private final DailyStatisticsService dailyStatisticsService;

    private final MonthlyStatisticsService monthlyStatisticsService;

    @GetMapping("/daily")
    public ResponseEntity<?> dailyStatisticsSetting(@RequestParam(defaultValue = "0")int day) {

        dailyStatisticsService.updateSales(day);
        dailyStatisticsService.updateCoupon(day);
        dailyStatisticsService.updateSettlement(day);

        return ResponseEntity.created(URI.create("/")).build();
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> monthlyStatisticsSetting
        (@RequestParam(defaultValue = "0") int year,
            @RequestParam(defaultValue = "0") int month) {

        monthlyStatisticsService.completeSettlement();
        monthlyStatisticsService.updateMonthlySum(year,month);
        monthlyStatisticsService.updateLocalCouponDownload();
        monthlyStatisticsService.updateCouponDownloadTop3(year,month);

        return ResponseEntity.created(URI.create("/")).build();
    }


}
