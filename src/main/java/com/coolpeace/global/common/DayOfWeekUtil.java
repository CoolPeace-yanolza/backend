package com.coolpeace.global.common;

import java.time.DayOfWeek;
import java.util.List;

public class DayOfWeekUtil {

    public static DayOfWeek fromDayOfWeekString(String str) {
        return switch (str) {
            case "월" -> DayOfWeek.MONDAY;
            case "화" -> DayOfWeek.TUESDAY;
            case "수" -> DayOfWeek.WEDNESDAY;
            case "목" -> DayOfWeek.THURSDAY;
            case "금" -> DayOfWeek.FRIDAY;
            case "토" -> DayOfWeek.SATURDAY;
            case "일" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalStateException("Unexpected value: " + str);
        };
    }

    public static String fromDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };
    }

    public static List<DayOfWeek> fromDayOfWeekStrings(List<String> strings) {
        return strings.stream().map(DayOfWeekUtil::fromDayOfWeekString).toList();
    }

    public static List<String> fromDayOfWeeks(List<DayOfWeek> dayOfWeeks) {
        return dayOfWeeks.stream().map(DayOfWeekUtil::fromDayOfWeek).toList();
    }
}
