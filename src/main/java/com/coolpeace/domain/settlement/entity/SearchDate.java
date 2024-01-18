package com.coolpeace.domain.settlement.entity;

import java.time.LocalDate;

public record SearchDate(int year, int month) {
    public static SearchDate getsearchDate() {
        LocalDate localDate = LocalDate.now().minusMonths(1);
        int year = localDate.getYear();
        int month= localDate.getMonthValue();
        return new SearchDate(year, month);
    }
}
