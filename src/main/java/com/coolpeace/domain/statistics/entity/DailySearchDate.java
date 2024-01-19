package com.coolpeace.domain.statistics.entity;

import java.time.LocalDate;

public record DailySearchDate(int year, int month,int day) {
    public static DailySearchDate getDailySearchDate(int year, int month,int day) {
        if (year == 0 || month == 0 || day == 0) {
            LocalDate localDate = LocalDate.now().minusDays(1);
            year = localDate.getYear();
            month = localDate.getMonthValue();
            day = localDate.getDayOfMonth();
        }
        return new DailySearchDate(year,month,day);
    }
}
