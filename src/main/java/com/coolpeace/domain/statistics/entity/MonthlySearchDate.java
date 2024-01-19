package com.coolpeace.domain.statistics.entity;

import java.time.LocalDate;

public record MonthlySearchDate(int year, int month) {
    public static MonthlySearchDate getMonthlySearchDate(int year,int month) {
        if (year == 0 || month == 0) {
            LocalDate localDate = LocalDate.now().minusMonths(1);
            year = localDate.getYear();
            month= localDate.getMonthValue();
        }
        return new MonthlySearchDate(year, month);
    }
}
