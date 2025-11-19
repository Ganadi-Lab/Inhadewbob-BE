package GanadiLab.inhadewbob.global.base.util;

import java.time.LocalDate;

public class DateUtil {

    // 이번 주 월요일
    public static LocalDate getStartOfWeek(LocalDate date) {
        return date.with(java.time.DayOfWeek.MONDAY);
    }

    // 이번 주 일요일
    public static LocalDate getEndOfWeek(LocalDate date) {
        return date.with(java.time.DayOfWeek.SUNDAY);
    }
}