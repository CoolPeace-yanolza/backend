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
    public ResponseEntity<?> dailyStatisticsSetting(
        @RequestParam(defaultValue = "0") int year,
        @RequestParam(defaultValue = "0") int month,
        @RequestParam(defaultValue = "0")int day) {

            dailyStatisticsService.updateSales(year,month,day);
            dailyStatisticsService.updateCoupon(year,month,day);
            dailyStatisticsService.updateSettlement(year,month,day);


        return ResponseEntity.created(URI.create("/")).build();
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> monthlyStatisticsSetting(
        @RequestParam(defaultValue = "0") int year,
        @RequestParam(defaultValue = "0") int month) {

            monthlyStatisticsService.updateMonthlySum(year,month);
            monthlyStatisticsService.updateLocalCouponDownload(year,month);
            monthlyStatisticsService.updateCouponDownloadTop3(year,month);
            monthlyStatisticsService.updateLocalCouponAvg(year,month);

        return ResponseEntity.created(URI.create("/")).build();
    }

    @GetMapping("/daily/all")
    public ResponseEntity<?> dailyStatisticsSettingAll(
        @RequestParam(defaultValue = "0") int year,
        @RequestParam(defaultValue = "0") int month) {

        for (int day = 1; day <= calculateDay(month); day++) {
            dailyStatisticsService.updateSales(year,month,day);
            dailyStatisticsService.updateCoupon(year,month,day);
            dailyStatisticsService.updateSettlement(year,month,day);
        }

        return ResponseEntity.created(URI.create("/")).build();
    }

    private int calculateDay(int month) {
        if (month == 2) return 28;
        if (month == 4 || month == 6 || month == 9 || month == 11) return 30;
        return 31;
    }

}
