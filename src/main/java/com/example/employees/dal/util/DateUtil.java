package com.example.employees.dal.util;

import java.time.LocalDate;

public class DateUtil {
    public static LocalDate getEarliestDate(LocalDate date1, LocalDate date2) {
        if (date1.isBefore(date2)) {
            return date1;
        } else {
            return date2;
        }
    }

    public static LocalDate getLatestDate(LocalDate date1, LocalDate date2) {
        if (date1.isAfter(date2)) {
            return date1;
        } else {
            return date2;
        }
    }
}
